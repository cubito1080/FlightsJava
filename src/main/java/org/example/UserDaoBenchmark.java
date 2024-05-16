package org.example;



import java.util.ArrayList;
import org.bson.Document;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class UserDaoBenchmark {

    private UserDao userDao;
    private Document userDoc;

    @Setup
    public void setup() {
        userDao = new UserDao();
        User user = new User("userName", new ArrayList<>());
        userDoc = userDao.createUserDocument(user);
    }

    @Benchmark
    public void insertUserBenchmark() {
        userDao.insertUser(userDoc);
    }
}
