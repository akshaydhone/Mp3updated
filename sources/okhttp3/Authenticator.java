package okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

public interface Authenticator {
    public static final Authenticator NONE = new C06641();

    /* renamed from: okhttp3.Authenticator$1 */
    class C06641 implements Authenticator {
        C06641() {
        }

        public Request authenticate(Route route, Response response) {
            return null;
        }
    }

    @Nullable
    Request authenticate(Route route, Response response) throws IOException;
}
