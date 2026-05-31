folder("Tools") {
    displayName("Tools")
    description("Folder for miscellaneous tools.")
}
freeStyleJob("Tools/clone-repository") {
    displayName("clone-repository")
    description("Git URL of the repository to clone")
    parameters {
        stringParam("GIT_REPOSITORY_URL", "", "Git URL of the repository to clone")
    }
    steps {
        shell("git clone \$GIT_REPOSITORY_URL")
    }
    wrappers {
        preBuildCleanup()
    }

}
freeStyleJob("Tools/SEED") {
    displayName("SEED")
    parameters {
        stringParam("GITHUB_NAME", "", "GitHub repository owner/repo_name '(e.g.: EpitechIT31000/chocolatine)'")
        stringParam("DISPLAY_NAME", "", "Display name for the job")
    }
    steps {
        dsl {
            text("""
                def GitHub_project_URL = "git@github.com:\${GITHUB_NAME}.git"
                job ("\${DISPLAY_NAME}") {
                    scm {
                        git("\${GitHub_project_URL}") {
                            remote {
                                github("\${GITHUB_NAME}")
                                credential("github-ssh-key")
                            }
                        }

                    }
                    properties {
                        githubProjectUrl("\${GitHub_project_URL}")
                    }
                    triggers {
                        scm("* * * * *")}
                    wrappers {
                        preBuildCleanup()
                    }
                    steps {
                        shell("make fclean")
                        shell("make")
                        shell("make tests_run")
                        shell("make clean")
                    }
                }
            """)
        }
    }
}