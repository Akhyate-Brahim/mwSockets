import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Distante extends Remote {
    public String echo() throws RemoteException;
}