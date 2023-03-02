package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTransferDaoTest extends BaseDaoTests {


    private JdbcTransferDao dao;

    @Before
    public void setUp(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcTransferDao(jdbcTemplate);

    }

    @Test
    void create() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findByToId() {
    }

    @Test
    void findByPayee() {
    }
}