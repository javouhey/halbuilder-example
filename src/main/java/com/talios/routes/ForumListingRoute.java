package com.talios.routes;

import com.google.inject.Inject;
import com.talios.geekzone.AuthStuff;
import com.talios.geekzone.Database;
import com.talios.geekzone.Forum;
import com.theoryinpractise.halbuilder.ResourceFactory;
import com.theoryinpractise.halbuilder.spi.Resource;
import spark.Request;
import spark.Response;
import spark.Route;

public class ForumListingRoute extends Route {

    public static final String PATH = "/forums";

    @Inject
    private Database database;

    @Inject
    private AuthStuff authStuff;

    @Inject
    private ResourceFactory resourceFactory;

    @Inject
    public ForumListingRoute() {
        super(PATH);
    }

    @Override
    public Object handle(Request request, Response response) {

        Resource resource = resourceFactory.newHalResource(PATH)
                                           .withLink(ForumCreationRoute.PATH, "create", authStuff.makeAuthPredicate("CUSTOMER CREATE", request));

        for (Forum forum : database.getForums().asMap().values()) {
            Resource forumResource = resourceFactory.newHalResource("/forums/" + forum.getId())
                                                    .withProperty("name", forum.getName())
                                                    .withProperty("description", forum.getDescription());

            resource.withSubresource("forum", forumResource);
        }

        response.status(203);  // not authoritative

        return resource.asRenderableResource().renderContent(request.headers("Accept"));
    }
}
