namespace motoProjectCSharp.domain;

public class Participant : Entity<long>
{
    public string Name { get; set; }
    public string IdNumber { get; set; }
    public long EngineSize { get; set; }

    public Participant(long id, string name, string idNumber, long engineSize) : base(id)
    {
        Name = name;
        IdNumber = idNumber;
        EngineSize = engineSize;
    }
}