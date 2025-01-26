package bg.nbu.cscb532.logistics.data.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    ADMIN,
    CASHIER,
    COURIER,
    USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
