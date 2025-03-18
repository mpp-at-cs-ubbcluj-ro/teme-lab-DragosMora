namespace motoProjectCSharp.repos;

using System.Collections.Generic;
using motoProjectCSharp.domain;
using Microsoft.Data.SqlClient;
using System.Linq;
using log4net;

public class TeamRepo : ITeamRepo
    {
        private readonly string _url;
        private static readonly ILog Logger = LogManager.GetLogger(typeof(TeamRepo));

        public TeamRepo(string url)
        {
            _url = url;
        }

        public void Save(Team team)
        {
            string query = "INSERT INTO teams (name) VALUES (@name)";
            try
            {
                using (SqlConnection conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (SqlCommand cmd = new SqlCommand(query, conn))
                    {
                        Logger.Info("Saving team: {team.Name}");
                        cmd.Parameters.AddWithValue("@name", team.Name);
                        cmd.ExecuteNonQuery();
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error("Error connecting: {e.Message}", e);
            }
        }

        public Team? FindById(long id)
        {
            string query1 = "SELECT * FROM teams WHERE id = @id";
            string query2 = "SELECT * FROM teams_participants WHERE team_id = @team_id";
            string query3 = "SELECT * FROM participants WHERE id IN ({0})";

            long? teamId = null;
            string? teamName = null;
            try
            {
                using (SqlConnection conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (SqlCommand cmd = new SqlCommand(query1, conn))
                    {
                        Logger.Info("Finding team by id: {id}");
                        cmd.Parameters.AddWithValue("@id", id);
                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                teamId = reader.GetInt64(reader.GetOrdinal("id"));
                                teamName = reader.GetString(reader.GetOrdinal("name"));
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error("Error connecting: {e.Message}", e);
            }

            if (!teamId.HasValue)
            {
                Logger.Info("Team not found: {id}");
                return null;
            }

            List<long> memberIds = new List<long>();
            try
            {
                using (SqlConnection conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (SqlCommand cmd = new SqlCommand(query2, conn))
                    {
                        Logger.Info("Finding members by team id: {id}");
                        cmd.Parameters.AddWithValue("@team_id", id);
                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                long memberId = reader.GetInt64(reader.GetOrdinal("participant_id"));
                                memberIds.Add(memberId);
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error("Error connecting: {e.Message}", e);
            }

            List<Participant> members = new List<Participant>();
            string placeholders = string.Join(",", memberIds.Select((memberId, index) => "@id" + index));
            string query3Formatted = string.Format(query3, placeholders);

            try
            {
                using (SqlConnection conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (SqlCommand cmd = new SqlCommand(query3Formatted, conn))
                    {
                        Logger.Info("Finding members in list");
                        for (int i = 0; i < memberIds.Count; i++)
                        {
                            cmd.Parameters.AddWithValue("@id" + i, memberIds[i]);
                        }
                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                long memberId = reader.GetInt64(reader.GetOrdinal("id"));
                                string memberName = reader.GetString(reader.GetOrdinal("name"));
                                string memberIdNumber = reader.GetString(reader.GetOrdinal("id_number"));
                                long memberEngineSize = reader.GetInt64(reader.GetOrdinal("engine_size"));
                                members.Add(new Participant(memberId, memberName, memberIdNumber, memberEngineSize));
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error("Error connecting: {e.Message}", e);
            }

            Team team = new Team(teamId.Value, teamName ?? "default", members);
            return team;
        }

        public List<Team> FindAll()
        {
            string query = "SELECT * FROM teams";
            List<Team> teams = new List<Team>();

            try
            {
                using (SqlConnection conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (SqlCommand cmd = new SqlCommand(query, conn))
                    {
                        Logger.Info("Finding all teams");
                        using (SqlDataReader reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                long teamId = reader.GetInt64(reader.GetOrdinal("id"));
                                Team? team = FindById(teamId);
                                if (team != null)
                                {
                                    teams.Add(team);
                                }
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error("Error connecting: {e.Message}", e);
            }

            return teams;
        }
    }