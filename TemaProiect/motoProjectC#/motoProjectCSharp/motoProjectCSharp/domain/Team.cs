namespace motoProjectCSharp.domain;

using System.Collections.Generic;

public class Team : Entity<long>
{
    public string Name { get; set; }
    public List<Participant> TeamMembers { get; set; }

    public Team(long id, string name, List<Participant> teamMembers) : base(id)
    {
        Name = name;
        TeamMembers = teamMembers;
    }
}