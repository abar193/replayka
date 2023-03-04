package me.mrabar.replayka;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QResponse is a Querydsl query type for QResponse
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QResponse extends com.querydsl.sql.RelationalPathBase<QResponse> {

    private static final long serialVersionUID = 512954818;

    public static final QResponse response = new QResponse("response");

    public final StringPath comment = createString("comment");

    public final SimplePath<Object> requestUuid = createSimple("requestUuid", Object.class);

    public final DateTimePath<java.sql.Timestamp> responseTs = createDateTime("responseTs", java.sql.Timestamp.class);

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public QResponse(String variable) {
        super(QResponse.class, forVariable(variable), "replayka", "response");
        addMetadata();
    }

    public QResponse(String variable, String schema, String table) {
        super(QResponse.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QResponse(String variable, String schema) {
        super(QResponse.class, forVariable(variable), schema, "response");
        addMetadata();
    }

    public QResponse(Path<? extends QResponse> path) {
        super(path.getType(), path.getMetadata(), "replayka", "response");
        addMetadata();
    }

    public QResponse(PathMetadata metadata) {
        super(QResponse.class, metadata, "replayka", "response");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(comment, ColumnMetadata.named("comment").withIndex(4).ofType(Types.VARCHAR).withSize(500));
        addMetadata(requestUuid, ColumnMetadata.named("request_uuid").withIndex(1).ofType(Types.OTHER).withSize(2147483647).notNull());
        addMetadata(responseTs, ColumnMetadata.named("response_ts").withIndex(2).ofType(Types.TIMESTAMP).withSize(29).withDigits(6));
        addMetadata(score, ColumnMetadata.named("score").withIndex(3).ofType(Types.INTEGER).withSize(10));
    }

}

