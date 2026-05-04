package vn.nlu.huypham.app.event.handler;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.FileSystemUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.event.ResourceDeleteEvent;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class ResourceHandler
{

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	void handleResoureDeleteEvent(
		ResourceDeleteEvent event)
	{
		log.info("Handling ResourceDeleteEvent for path: {}", event.pathToDelete());
		event.pathToDelete().forEach((
			folder) ->
		{
			try
			{
				boolean success = FileSystemUtils.deleteRecursively(folder);

				if (success)
				{
					log.info("Successfully deleted storage: {}", folder);
				} else
				{
					log.warn("Folder does not exist or could not be deleted: {}", folder);
				}
			}
			catch (IOException e)
			{
				log.error("Failed to delete resource folder at {}: {}", folder, e.getMessage());
			}
		});
	}
}
