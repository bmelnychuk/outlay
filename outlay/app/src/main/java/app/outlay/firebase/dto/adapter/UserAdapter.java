package app.outlay.firebase.dto.adapter;

import com.google.firebase.auth.FirebaseUser;
import app.outlay.domain.model.User;

/**
 * Created by bmelnychuk on 2/9/17.
 */

public class UserAdapter {
    public static User fromFirebaseUser(FirebaseUser fbUser) {
        User result = new User();
        result.setAnonymous(fbUser.isAnonymous());
        result.setUserName(fbUser.getDisplayName());
        result.setId(fbUser.getUid());
        result.setEmail(fbUser.getEmail());
        return result;
    }
}
