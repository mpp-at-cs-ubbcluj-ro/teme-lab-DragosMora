using motoProjectCSharp.domain;

namespace motoProjectCSharp.repos;
using System.Collections.Generic;

public interface IRaceRepo : IRepo<Race, long>
{
    new void Save(Race entity);
        
    new Race? FindById(long id);
        
    new List<Race> FindAll();
        
    void SignUpParticipant(Participant participant, Race race);
}