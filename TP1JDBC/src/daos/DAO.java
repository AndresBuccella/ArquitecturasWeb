package daos;

import java.util.Iterator;
import java.util.Optional;

public interface DAO<T> {
	public Optional<T> get(long id);
	public Iterator<T> getAll();
	public void add(T t);
	public void update(long id, T t);
	public void delete(long id);
}
