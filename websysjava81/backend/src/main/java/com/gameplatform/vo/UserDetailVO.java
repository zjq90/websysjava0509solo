package com.gameplatform.vo;

import com.gameplatform.entity.*;
import lombok.Data;
import java.util.List;

@Data
public class UserDetailVO {
    private User user;
    private List<GameRecord> gameRecords;
    private List<Favorite> favorites;
    private List<Comment> comments;
    private List<LoginDevice> loginDevices;
    private List<UserBehavior> behaviors;
    private List<BanRecord> banRecords;
}
