package vn.nlu.huypham.app.event;

import java.nio.file.Path;
import java.util.List;

public record ResourceDeleteEvent(List<Path> pathToDelete)
{
}
