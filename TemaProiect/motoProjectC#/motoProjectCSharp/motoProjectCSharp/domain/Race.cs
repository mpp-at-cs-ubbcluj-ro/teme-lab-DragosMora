namespace motoProjectCSharp.domain;

using System.Collections.Generic;

public class Race : Entity<long>
{
    public string Name { get; set; }
    public long EngineSize { get; set; }
    public string Date {get; set; }
    public List<Participant> Participants { get; set; }

    public Race(long id, string name, long engineSize, string date, List<Participant> participants) : base(id)
    {
        Name = name;
        EngineSize = engineSize;
        Date = date;
        Participants = participants;
    }
}