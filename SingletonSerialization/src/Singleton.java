import java.io.Serializable;

class Singleton implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7261648823508968508L;
	private transient static Singleton instance;

	private Singleton() {
		System.out.println("Here is constructor");
	}

	public static synchronized Singleton getInstance() {
		if (instance == null) {
			instance = new Singleton();
		}
		return instance;
	}
	private Object readResolve()
    {
        return instance;
    }
}