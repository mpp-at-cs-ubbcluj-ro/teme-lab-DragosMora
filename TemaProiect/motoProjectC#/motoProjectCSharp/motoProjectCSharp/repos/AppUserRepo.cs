namespace motoProjectCSharp.repos;
using System;
using Microsoft.Data.SqlClient;
using log4net;

public class AppUserRepo : IAppUserRepo
{
    private readonly string _url;
    private static readonly ILog Logger = LogManager.GetLogger(typeof(AppUserRepo));

    public AppUserRepo(string url)
    {
        _url = url;
    }

    public string? FindPasswordByUsername(string username)
    {
        string query = "SELECT password FROM users WHERE username = ?";
        try
        {
            using (var conn = new SqlConnection(_url))
            {
                conn.Open();
                Logger.Info("Successfully connected to the database!");
                using (var stmt = new SqlCommand(query, conn))
                {
                    stmt.Parameters.AddWithValue("@username", username);
                    var result = stmt.ExecuteScalar();
                    if (result != null)
                    {
                        Logger.Info("Password found!");
                        return result.ToString();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Logger.Error($"Error connecting: {e.Message}", e);
            return null;
        }

        return null;
    }
}