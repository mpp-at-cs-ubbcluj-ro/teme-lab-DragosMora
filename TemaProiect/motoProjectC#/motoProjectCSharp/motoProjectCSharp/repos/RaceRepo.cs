namespace motoProjectCSharp.repos;

using System.Collections.Generic;
using motoProjectCSharp.domain;
using Microsoft.Data.SqlClient;
using System.Linq;
using log4net;


public class RaceRepo : IRaceRepo
    {
        private readonly string _url;
        private static readonly ILog Logger = LogManager.GetLogger(typeof(RaceRepo));

        public RaceRepo(string url)
        {
            _url = url;
        }

        public void Save(Race race)
        {
            string query = "INSERT INTO races (name, engine_size, date) VALUES (@name, @engine_size, @date)";
            try
            {
                using (var conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (var cmd = new SqlCommand(query, conn))
                    {
                        Logger.Info($"Saving race: {race}");
                        cmd.Parameters.AddWithValue("@name", race.Name);
                        cmd.Parameters.AddWithValue("@engine_size", race.EngineSize);
                        cmd.Parameters.AddWithValue("@date", race.Date);
                        cmd.ExecuteNonQuery();
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error($"Error saving race: {e.Message}", e);
            }
        }

        public Race? FindById(long id)
        {
            string query1 = "SELECT * FROM races WHERE id = @id";
            string query2 = "SELECT * FROM races_participants WHERE race_id = @race_id";

            long raceId = 0, engineSize = 0;
            string? name = null, date = null;
            try
            {
                using (var conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (var cmd = new SqlCommand(query1, conn))
                    {
                        Logger.Info($"Finding race by id: {id}");
                        cmd.Parameters.AddWithValue("@id", id);
                        using (var reader = cmd.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                raceId = reader.GetInt64(reader.GetOrdinal("id"));
                                name = reader.GetString(reader.GetOrdinal("name"));
                                engineSize = reader.GetInt64(reader.GetOrdinal("engine_size"));
                                date = reader.GetString(reader.GetOrdinal("date"));
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error($"Error finding race by id: {e.Message}", e);
            }

            if (raceId == 0)
            {
                Logger.Info($"Race not found: {id}");
                return null;
            }

            List<long> participantIds = new List<long>();
            try
            {
                using (var conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (var cmd = new SqlCommand(query2, conn))
                    {
                        Logger.Info($"Finding participants by race id: {id}");
                        cmd.Parameters.AddWithValue("@race_id", id);
                        using (var reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                participantIds.Add(reader.GetInt64(reader.GetOrdinal("participant_id")));
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error($"Error finding participants by race id: {e.Message}", e);
            }

            List<Participant> participants = new List<Participant>();
            string placeholders = string.Join(",", participantIds.Select((participantId, index) => "@id" + index));
            string query3 = "SELECT * FROM participants WHERE id IN (" + placeholders + ")";
            try
            {
                using (var conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (var cmd = new SqlCommand(query3, conn))
                    {
                        Logger.Info("Finding participants in list!");
                        for (int i = 0; i < participantIds.Count; i++)
                        {
                            cmd.Parameters.AddWithValue("@id" + i, participantIds[i]);
                        }
                        using (var reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                var participantId = reader.GetInt64(reader.GetOrdinal("id"));
                                var participantName = reader.GetString(reader.GetOrdinal("name"));
                                var participantIdNumber = reader.GetString(reader.GetOrdinal("id_number"));
                                var participantEngineSize = reader.GetInt64(reader.GetOrdinal("engine_size"));
                                participants.Add(new Participant(participantId, participantName, participantIdNumber, participantEngineSize));
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error($"Error finding participants: {e.Message}", e);
            }

            return new Race(raceId, name ?? "default", engineSize, date ?? "default", participants);
        }

        public List<Race> FindAll()
        {
            string query = "SELECT * FROM races";
            List<Race> races = new List<Race>();
            try
            {
                using (var conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (var cmd = new SqlCommand(query, conn))
                    {
                        Logger.Info("Finding all races!");
                        using (var reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                var raceId = reader.GetInt64(reader.GetOrdinal("id"));
                                var race = FindById(raceId);
                                if (race != null)
                                {
                                    races.Add(race);
                                }
                            }
                        }
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error($"Error finding races: {e.Message}", e);
            }
            return races;
        }

        public void SignUpParticipant(Participant participant, Race race)
        {
            string query = "INSERT INTO races_participants (race_id, participant_id) VALUES (@race_id, @participant_id)";
            try
            {
                using (var conn = new SqlConnection(_url))
                {
                    conn.Open();
                    using (var cmd = new SqlCommand(query, conn))
                    {
                        Logger.Info($"Signing up participant: {participant}");
                        cmd.Parameters.AddWithValue("@race_id", race.Id);
                        cmd.Parameters.AddWithValue("@participant_id", participant.Id);
                        cmd.ExecuteNonQuery();
                    }
                }
            }
            catch (SqlException e)
            {
                Logger.Error($"Error signing up participant: {e.Message}", e);
            }
        }
    }