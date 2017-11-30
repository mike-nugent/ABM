package tasks;

public class Task 
{
	private long startTime;
	private long endTime;
	private String errorMessage;
	private TaskState state;
	private double percentComplete; 
	private String infoString;
	
	public Task()
	{
		state = TaskState.Running;
		startTime = System.currentTimeMillis();
	}
	
	public synchronized void setTaskState(TaskState state)
	{
		this.state = state;
		if(state.isTerminal())
		{
			endTime = System.currentTimeMillis();
		}
	}
	
	public TaskState getTaskState()
	{
		return this.state;
	}
	
	public boolean isTerminal()
	{
		return state.isTerminal();
	}

	public String getErrorMessage() 
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) 
	{
		this.errorMessage = errorMessage;
	}

	public long getEndTime() 
	{
		return endTime;
	}

	public void setEndTime(long endTime) 
	{
		this.endTime = endTime;
	}

	public long getStartTime() 
	{
		return startTime;
	}

	public void setStartTime(long startTime) 
	{
		this.startTime = startTime;
	}
	
	public void setPercentComplete(double val)
	{
		this.percentComplete = val;
	}

	public double getPercentComplete() 
	{
		return this.percentComplete;
	}

	public String getInfoString() {
		return infoString;
	}

	public void setInfoString(String infoString) {
		this.infoString = infoString;
	}
}
