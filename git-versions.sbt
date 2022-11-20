// Git versioning
enablePlugins(GitVersioning)

ThisBuild / git.useGitDescribe       := true
ThisBuild / git.uncommittedSignifier := None
ThisBuild / versionScheme            := Some("pvp")

def _isSnapshotByGit: Def.Initialize[Boolean] =
  Def.setting(git.gitCurrentTags.value.isEmpty || git.gitUncommittedChanges.value)

ThisBuild / version := (ThisBuild / version).value + (if (_isSnapshotByGit.value)
                                                        "-SNAPSHOT"
                                                      else
                                                        "")
