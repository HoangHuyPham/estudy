package vn.nlu.huypham.app.service;

import java.nio.file.Path;
import vn.nlu.huypham.app.exception.custom.AppException;

public interface FFmpegService
{
	int getDurationInSeconds(
		Path videoPath) throws AppException;
}
