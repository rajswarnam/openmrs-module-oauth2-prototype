package org.openmrs.module.oauth2.api.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.oauth2.Client;
import org.openmrs.module.oauth2.api.impl.UserCredentialsServiceImpl;
import org.openmrs.module.oauth2.api.model.*;
import org.springframework.security.core.GrantedAuthority;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Maps fields required from {@link org.openmrs.module.oauth2.Client} to Set<String> used by Spring Security OAuth2
 * Earlier fields such as Client.redirect_uri,Client.scope were declared as @ElementCollection of Set<String>
 * This created issues for writing test data for DBUnit as @ElementCollection of sets does not support primary_key
 * while DBUnit in core is configured to have primary_key for every table.
 * <p/>
 * Created by OPSKMC on 8/19/15.
 */
public class ClientSpringOAuthUtils {

	private static final Log log = LogFactory.getLog(ClientSpringOAuthUtils.class);

    public static Set<String> parseResources(Collection<Resource> resources) {
    	if(log.isDebugEnabled())
    		log.info("Entering parseResource");
        Set<String> resourceIdSet = new HashSet<String>();
        for (Resource resource : resources) {
            resourceIdSet.add(resource.getResource());
        }
        return resourceIdSet;
    }

    public static Collection<GrantedAuthority> parseAuthorities(Collection<CustomGrantedAuthority> authorities) {
    	if(log.isDebugEnabled())
    		log.info("Entering Collection()");
        if(authorities==null)
            return new HashSet<GrantedAuthority>();
        Collection<GrantedAuthority> authoritySet = new HashSet<GrantedAuthority>();
        for (CustomGrantedAuthority grantedAuthority : authorities) {
            authoritySet.add(grantedAuthority);
        }
        return authoritySet;
    }

    public static Set<String> parseRedirectURIs(Collection<RedirectURI> redirectURIs) {
    	if(log.isDebugEnabled())
    		log.info("Entering parseRedirectURIs");
        Set<String> redirectURISet = new HashSet<String>();
        for (RedirectURI redirectURI : redirectURIs) {
            redirectURISet.add(redirectURI.getRedirectURI());
        }
        return redirectURISet;
    }

    public static Set<String> parseAuthorizedGrantTypes(Collection<AuthorizedGrantType> authorizedGrantTypes) {
    	if(log.isDebugEnabled())
    		log.info("Entering parseAuthorizedGrantTypes");
        Set<String> authorizedGrantTypeSet = new HashSet<String>();
        for (AuthorizedGrantType authorizedGrantType : authorizedGrantTypes) {
            authorizedGrantTypeSet.add(authorizedGrantType.getAuthorizedGrantType());
        }
        return authorizedGrantTypeSet;
    }

    public static Set<String> parseScope(Collection<Scope> scopes) {
    	if(log.isDebugEnabled())
    		log.info("Entering parseScope");
        Set<String> scopeSet = new HashSet<String>();
        for (Scope scope : scopes) {
            scopeSet.add(scope.getScope());
        }
        return scopeSet;
    }

    /**
     * todo catch individual exceptions
     *
     * @param commaDelimitedString
     * @param elementClass
     * @param <T>
     * @return
     */
    public static <T> Collection<T> commaDelimitedStringToCollection(String commaDelimitedString, Client client, Class<T> elementClass) {
    	if(log.isDebugEnabled())
    		log.info("Entering commaDelimitedStringToCollection");
        Collection<T> collection = new HashSet<T>();
        String[] elementArray = commaDelimitedString.split(",");
        for (String element : elementArray) {
            try {
                T instance = elementClass.newInstance();
                Method setParameter = elementClass.getMethod("setParameter", String.class);
                Method setClient = elementClass.getMethod("setClient", Client.class);
                setParameter.invoke(instance, element);
                setClient.invoke(instance, client);
                collection.add(instance);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return collection;
    }
}
