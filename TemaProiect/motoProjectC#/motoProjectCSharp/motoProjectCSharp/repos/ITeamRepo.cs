using motoProjectCSharp.domain;
namespace motoProjectCSharp.repos;
using System.Collections.Generic;

public interface ITeamRepo : IRepo<Team, long>
{
    new void Save(Team entity);

    new Team? FindById(long id);
        
    new List<Team> FindAll();
}