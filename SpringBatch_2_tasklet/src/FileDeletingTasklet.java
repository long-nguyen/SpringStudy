import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/***
 * A tasklet is a batch that run a single task(like clean up..)
 * @author long.nguyen-tien
 *
 */
public class FileDeletingTasklet implements Tasklet, InitializingBean{

	private Resource directory;
	
	public Resource getDirectory() {
		return directory;
	}
	
	public void setDirectory(Resource dir) {
		this.directory = dir;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(directory, "directory must be set");
	}

	/***
	 * Main tasklet logic
	 */
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1)
			throws Exception {
		File dir = directory.getFile();
		Assert.state(dir.isDirectory());
		File []files = dir.listFiles();
		
		return null;
	}

}
