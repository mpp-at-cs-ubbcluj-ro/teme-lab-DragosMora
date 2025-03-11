namespace motoProjectCSharp.domain;

public class AppUser : Entity<long>
{
    public string Name { get; set; }
    public string Password { get; set; }

    public AppUser(long id, string name, string password) : base(id)
    {
        Name = name;
        Password = password;
    }
}