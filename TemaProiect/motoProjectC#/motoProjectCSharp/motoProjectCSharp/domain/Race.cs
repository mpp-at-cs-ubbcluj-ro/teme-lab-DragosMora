namespace motoProjectCSharp.domain;

using System.Collections.Generic;

public class Race : Entity<long>
{
    public string Name { get; set; }
    public long EngineSize { get; set; }
    public long ParticipantNumber { get; set; }
    public List<Participant> Participants { get; set; } = new List<Participant>();

    public Race(long id, long engineSize, long participantNumber) : base(id)
    {
        EngineSize = engineSize;
        ParticipantNumber = participantNumber;
    }
}