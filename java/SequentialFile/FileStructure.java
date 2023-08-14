public interface FileStructure {
    public void create(int id, long gamePosition);
    public long read(int id);
    public boolean delete(int id);
    public boolean update(int id, long gamePosition);
}
