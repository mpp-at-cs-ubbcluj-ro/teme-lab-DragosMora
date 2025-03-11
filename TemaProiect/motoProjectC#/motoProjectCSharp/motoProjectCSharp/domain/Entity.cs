namespace motoProjectCSharp.domain;

public class Entity<T>
{
    public T Id { get; }

    public Entity(T entityId)
    {
        Id = entityId;
    }
}
