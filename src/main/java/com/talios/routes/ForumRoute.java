package com.talios.routes;

import com.google.inject.Inject;
import com.talios.geekzone.Database;
import com.talios.geekzone.Forum;
import com.theoryinpractise.halbuilder.ResourceFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class ForumRoute extends Route {

    public static final String PATH = "/forums/:id";

    private Database database;

    @Inject private ResourceFactory resourceFactory;

    @Inject
    public ForumRoute(Database database) {
        super(PATH);
        this.database = database;
    }

    @Override
    public Object handle(Request request, Response response) {

        final String id = request.params("id");

        if (database.getForums().asMap().containsKey(id)) {
            Forum forum = database.getForums().getIfPresent(id);

            return resourceFactory.newHalResource("/forums/" + forum.getId())
                                 .withLink("forums", "/forums")
                                 .withBean(forum)
                                 .asRenderableResource()
                                 .renderContent(request.headers("Accept"));

        } else {
            halt(404, String.format("Panic! Unknown forum with code '%s'", id));
        }

        return null;
    }
}
