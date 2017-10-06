package com.azazellj.githubwatcher.common;

import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.data.model.User;
import com.azazellj.githubwatcher.data.response.RepositoryResponse;

import org.joda.time.DateTime;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by azazellj on 10/5/17.
 */

public class MockModelFabric {
    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static String randomEmail() {
        final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        final int emailLength = 15;
        final int emailPosition = 7;
        final int dotPosition = 12;

        StringBuilder sb = new StringBuilder(emailLength);
        for (int i = 0; i < emailLength; i++) {
            if (i == emailPosition) {
                sb.append('@');
            } else if (i == dotPosition) {
                sb.append(".");
            } else {
                sb.append(chars.charAt(rnd.nextInt(chars.length())));
            }
        }
        return sb.toString();
    }

    public static boolean randomBoolean() {
        return Math.random() < 0.5;
    }

    public static int randomInt() {
        return (int) (Math.random() * (Short.MAX_VALUE - Short.MIN_VALUE));
    }

    public static float randomFloat() {
        return (float) Math.random() * (Short.MAX_VALUE - Short.MIN_VALUE);
    }

    public static Date randomDate() {
        return Calendar.getInstance().getTime();
    }

    public static RepositoryResponse randomRepositories(int repoCount) {
        RepositoryResponse response = new RepositoryResponse();
        List<Repository> repositories = new ArrayList<>();

        for (int i = 0; i < repoCount; i++) {
            repositories.add(randomRepository());
        }

        response.setItems(repositories);

        return response;
    }

    public static Repository randomRepository() {
        Repository repository = new Repository();
        repository.setDescription(randomString());
        repository.setForksCount(randomInt());
        repository.setFullName(randomString());
        repository.setName(randomString());
        repository.setOwner(randomUser());
        repository.setUpdatedAt(new DateTime(randomDate()).toString());

        return repository;
    }

    public static User randomUser() {
        User user = new User();
        user.setAvatarUrl("https://www.allaboutbirds.org/guide/PHOTO/LARGE/fish_crow_glamor_bruce_van_valen.jpg");
        user.setHtmlUrl("https://api.github.com/repos/square/retrofit/subscribers");
        user.setLogin(randomString());

        return user;
    }
}
