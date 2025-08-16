package com.sastudios.tmdbproxy.controller;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("/api/images")
public class ImageReverseProxyController {

    // TMDB image base
    private static final String TMDB_IMAGE_ORIGIN = "https://image.tmdb.org/t/p";

    // Allow-list of sizes TMDB supports (add/remove as needed)
    private static final Set<String> ALLOWED_SIZES = Set.of(
            "w92","w154","w185","w342","w500","w780","w1280","original"
    );

    // Reasonable network timeouts (ms)
    private static final int CONNECT_TIMEOUT_MS = 4000;
    private static final int READ_TIMEOUT_MS = 8000;

    /**
     * Matches /api/images/{size}/**  → captures the remaining path (with slashes).
     * Example app URL you’ll call from Android/Coil:
     *   https://your-backend.com/api/images/w780/abc123.jpg
     */
    @GetMapping("/{size}/**")
    public ResponseEntity<StreamingResponseBody> proxy(
            @PathVariable String size,
            HttpServletRequest request
    ) throws IOException {

        // Validate size
        if (!ALLOWED_SIZES.contains(size)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(os -> os.write(("Invalid size: " + size).getBytes(StandardCharsets.UTF_8)));
        }

        // Extract the part after /api/images/{size}/
        String pathWithinHandler = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String remainder = new org.springframework.util.AntPathMatcher().extractPathWithinPattern(bestPattern, pathWithinHandler);

        if (remainder == null || remainder.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(os -> os.write("Missing image path".getBytes(StandardCharsets.UTF_8)));
        }

        // Normalize: remove any leading slash and reject simple traversal
        String imagePath = remainder.startsWith("/") ? remainder.substring(1) : remainder;
        if (imagePath.contains("..")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(os -> os.write("Invalid path".getBytes(StandardCharsets.UTF_8)));
        }

        // Some clients pass still_path beginning with a slash; handle both safely
        // URL-encode each path segment to be safe
        String encodedPath = encodeEachSegment(imagePath);

        // Build TMDB target URL
        String target = TMDB_IMAGE_ORIGIN + "/" + size + "/" + encodedPath;

        HttpURLConnection conn = null;
        try {
            URL url = new URL(target);
            conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
            conn.setReadTimeout(READ_TIMEOUT_MS);
            conn.setRequestMethod("GET");
            //conn.setRequestProperty("User-Agent", "YourApp-ImageProxy/1.0");
            conn.connect();

            int status = conn.getResponseCode();
            // Pass-through TMDB errors as-is
            if (status >= 400) {
                InputStream err = conn.getErrorStream();
                return ResponseEntity.status(status)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(os -> {
                            if (err != null) err.transferTo(os);
                            else os.write(("Upstream error " + status).getBytes(StandardCharsets.UTF_8));
                        });
            }

            String contentType = Optional.ofNullable(conn.getContentType())
                    .orElse("application/octet-stream");
            int contentLength = conn.getContentLength();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            if (contentLength > 0) {
                headers.setContentLength(contentLength);
            }

            // Optional: let client-side (Coil) cache using TMDB’s headers, without server storage
            // Forward a few caching headers if present
            copyHeader(conn, headers, "Cache-Control");
            copyHeader(conn, headers, "ETag");
            copyHeader(conn, headers, "Last-Modified");
            copyHeader(conn, headers, "Expires");

            // Extra safety headers (don’t affect caching)
            headers.add("X-Proxy-Origin", "tmdb");
            headers.add("X-Content-Type-Options", "nosniff");

            InputStream upstream = conn.getInputStream();
            StreamingResponseBody body = outputStream -> stream(upstream, outputStream);

            return new ResponseEntity<>(body, headers, HttpStatus.OK);

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(os -> os.write(("Image fetch failed: " + ex.getMessage()).getBytes(StandardCharsets.UTF_8)));
        } finally {
            // InputStream is closed by StreamingResponseBody after transfer
            // HttpURLConnection disconnect happens automatically when streams close,
            // but we can still call disconnect defensively.
            // (Don’t disconnect before the stream finishes.)
        }
    }

    private static void copyHeader(URLConnection conn, HttpHeaders headers, String name) {
        String v = conn.getHeaderField(name);
        if (v != null) headers.add(name, v);
    }

    private static void stream(InputStream in, OutputStream out) throws IOException {
        try (in; out) {
            byte[] buf = new byte[16 * 1024];
            int r;
            while ((r = in.read(buf)) != -1) {
                out.write(buf, 0, r);
            }
            out.flush();
        }
    }

    private static String encodeEachSegment(String path) {
        String[] parts = path.split("/");
        List<String> encoded = new ArrayList<>(parts.length);
        for (String p : parts) {
            // Keep empty segments from accidental double slashes out
            if (p.isEmpty()) continue;
            encoded.add(URLEncoder.encode(p, StandardCharsets.UTF_8)
                    .replace("+", "%20")); // keep spaces encoded as %20
        }
        return String.join("/", encoded);
    }
}
