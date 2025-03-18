
namespace motoProjectCSharp.repos;

public interface IAppUserRepo
{
    string FindPasswordByUsername(string username);
}