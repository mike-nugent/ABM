package tasks;

public enum TaskState 
{
	Running  	(false),
	Completed 	(true),
	Error		(true); 
	
	private boolean _isTerminal;

	private TaskState(boolean isTerminal)
	{
		_isTerminal = isTerminal;
	}
	
	public boolean isTerminal()
	{
		return _isTerminal;
	}
}
