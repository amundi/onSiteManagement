package com.amundi.tech.onsite.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/git")
@Tag(name = "git", description = "manage git informations")
public class CommitInfoController {

    @Value("${git.commit.message.short}")
    private String commitMessage;

    @Value("${git.branch}")
    private String branch;

    @Value("${git.commit.id}")
    private String commitId;

    @Value("${git.commit.time}")
    private String commitTime;

    @Value("${git.commit.user.email}")
    private String commitUserEmail;

    @Value("${git.closest.tag.name}")
    private String closestTagName;

    @GetMapping("/infos")
    public Map<String, String> getCommitId() {
        Map<String, String> result = new HashMap<>();
        result.put("Commit message",commitMessage);
        result.put("Commit branch", branch);
        result.put("Commit id", commitId);
        result.put("Commit time", commitTime);
        result.put("Commit usermail", commitUserEmail);
        result.put("Closest tag name", closestTagName);
        return result;
    }
}