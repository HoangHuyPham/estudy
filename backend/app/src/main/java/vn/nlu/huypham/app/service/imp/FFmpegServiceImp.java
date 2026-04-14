package vn.nlu.huypham.app.service.imp;

import java.nio.file.Path;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.constant.Errors;
import vn.nlu.huypham.app.exception.custom.AppException;
import vn.nlu.huypham.app.service.FFmpegService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class FFmpegServiceImp implements FFmpegService {
    @Override
    public int getDurationInSeconds(Path videoPath) throws AppException {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    videoPath.toString());
            Process process = pb.start();
            String output = new String(process.getInputStream().readAllBytes()).trim();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                String errorOutput = new String(process.getErrorStream().readAllBytes()).trim();
                log.error("ffprobe error: {}", errorOutput);
                throw Errors.FFMPEG_ERROR;
            }
            return (int) Math.round(Double.parseDouble(output));
        } catch (Exception e) {
            log.error("Failed to get video duration", e);
            throw Errors.FFMPEG_ERROR;
        }
    }
}
