package main.java.dao;

import java.sql.Connection;
import java.util.Optional;

public interface DAO<T> {
	void createTable(Connection conn);
	void dropTable(Connection conn);

	Optional<T> get(Connection conn, int id);
	Iterable<T> getAll(Connection conn);
	void save(Connection conn, T t);
	void update(Connection conn, T t);
	void delete(Connection conn, int id);
}
