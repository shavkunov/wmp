package com.qreal.wmp.dashboard.database.robots.client;

import com.qreal.wmp.dashboard.database.exceptions.Aborted;
import com.qreal.wmp.dashboard.database.exceptions.ErrorConnection;
import com.qreal.wmp.dashboard.database.exceptions.NotFound;
import com.qreal.wmp.dashboard.database.robots.model.Robot;
import com.qreal.wmp.thrift.gen.TRobot;
import org.jetbrains.annotations.NotNull;

/** RobotDBService interface.*/
public interface RobotService {

    /**
     * Saves robot.
     *
     * @param robot robot to save (Id must not be set)
     */
    long register(@NotNull Robot robot) throws Aborted, ErrorConnection;

    /**
     * Register robot with specified owner.
     *
     * @param robot    robot to register (Id must not be set)
     * @param username owner of robot
     */
    void registerByUsername(@NotNull Robot robot, String username) throws Aborted, ErrorConnection;

    /**
     * Finds robot with specified Id.
     *
     * @param id id of robot to find
     */
    @NotNull
    Robot findById(long id) throws NotFound, ErrorConnection;

    /**
     * Test if exists robot with specified Id.
     *
     * @param id of robot to test if exists
     */
    boolean isRobotExists(long id) throws ErrorConnection;

    /**
     * Deletes robot.
     *
     * @param id id of robot to delete
     */
    void delete(long id) throws Aborted, ErrorConnection;

    /**
     * Update robot.
     *
     * @param robot robot to update (Id must be set correctly)
     */
    void update(@NotNull TRobot robot) throws Aborted, ErrorConnection;
}