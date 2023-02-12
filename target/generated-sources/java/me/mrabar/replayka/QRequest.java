package me.mrabar.replayka;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QRequest is a Querydsl query type for QRequest
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QRequest extends com.querydsl.sql.RelationalPathBase<QRequest> {

    private static final long serialVersionUID = 707576046;

    public static final QRequest request = new QRequest("request");

    public final NumberPath<Integer> blogId = createNumber("blogId", Integer.class);

    public final SimplePath<Object> info = createSimple("info", Object.class);

    public final StringPath page = createString("page");

    public final SimplePath<Object> requestUuid = createSimple("requestUuid", Object.class);

    public final DateTimePath<java.sql.Timestamp> time = createDateTime("time", java.sql.Timestamp.class);

    public final com.querydsl.sql.PrimaryKey<QRequest> requestPk = createPrimaryKey(requestUuid);

    public final com.querydsl.sql.ForeignKey<QBlog> requestBlogFk = createForeignKey(blogId, "blog_id");

    public final com.querydsl.sql.ForeignKey<QResponse> _responseRequestFk = createInvForeignKey(requestUuid, "request_uuid");

    public QRequest(String variable) {
        super(QRequest.class, forVariable(variable), "replayka", "request");
        addMetadata();
    }

    public QRequest(String variable, String schema, String table) {
        super(QRequest.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QRequest(String variable, String schema) {
        super(QRequest.class, forVariable(variable), schema, "request");
        addMetadata();
    }

    public QRequest(Path<? extends QRequest> path) {
        super(path.getType(), path.getMetadata(), "replayka", "request");
        addMetadata();
    }

    public QRequest(PathMetadata metadata) {
        super(QRequest.class, metadata, "replayka", "request");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(blogId, ColumnMetadata.named("blog_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(info, ColumnMetadata.named("info").withIndex(4).ofType(Types.OTHER).withSize(2147483647));
        addMetadata(page, ColumnMetadata.named("page").withIndex(5).ofType(Types.VARCHAR).withSize(75));
        addMetadata(requestUuid, ColumnMetadata.named("request_uuid").withIndex(2).ofType(Types.OTHER).withSize(2147483647).notNull());
        addMetadata(time, ColumnMetadata.named("time").withIndex(3).ofType(Types.TIMESTAMP).withSize(29).withDigits(6));
    }

}

