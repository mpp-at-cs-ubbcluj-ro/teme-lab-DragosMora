namespace motoProjectCSharp.repos;

using System.Collections.Generic;

public interface IRepo<T, ID>
{
    void Save(T entity);
    T? FindById(ID id);
    List<T> FindAll();
    //void Update(T entity);
    //void DeleteById(ID id);
}