package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;
    private PlayerDao playerDao;

    public GameStateDaoJdbc(DataSource dataSource, PlayerDao playerDao) {
        this.dataSource = dataSource;
        this.playerDao = playerDao;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (current_map, other_map, saved_at, player_id, save_name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setString(2, state.getOtherMap());
            statement.setDate(3, state.getSavedAt());
            statement.setInt(4, state.getPlayer().getId());
            statement.setString(5, state.getSaveName());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state, String saveName) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET current_map = ?, other_map = ?, saved_at = ?, " +
                    "player_id = ?, save_name = ? WHERE save_name = ?";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, state.getCurrentMap());
            statement.setString(2, state.getOtherMap());
            statement.setDate(3, state.getSavedAt());
            statement.setInt(4, state.getPlayer().getId());
            statement.setString(5, state.getSaveName());
            statement.setString(6, saveName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM game_state WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return null;
            PlayerModel playerModel = playerDao.get(resultSet.getInt("player_id"));
            return new GameState(resultSet.getString("current_map"), resultSet.getString("other_map"),
                    resultSet.getDate("saved_at"), playerModel, resultSet.getString("save_name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        List<GameState> games = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM game_state";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return null;
            do {
                PlayerModel playerModel = playerDao.get(resultSet.getInt("player_id"));
                GameState gameState = new GameState(resultSet.getInt("id"), resultSet.getString("current_map"), resultSet.getString("other_map"),
                        resultSet.getDate("saved_at"), playerModel, resultSet.getString("save_name"));
                games.add(gameState);
            } while (resultSet.next());
            return games;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GameState> getAllIdAndName() {
        List<GameState> games = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, save_name FROM game_state";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) return null;
            do {
                GameState gameState = new GameState(resultSet.getInt("id"), resultSet.getString("save_name"));
                games.add(gameState);
            } while (resultSet.next());
            return games;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
