package app.outlay.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by bmelnychuk on 10/27/16.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {
}
