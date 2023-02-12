package me.mrabar.replayka;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QBlog is a Querydsl query type for QBlog
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QBlog extends com.querydsl.sql.RelationalPathBase<QBlog> {

    private static final long serialVersionUID = -505473629;

    public static final QBlog blog = new QBlog("blog");

    public final NumberPath<Integer> blogId = createNumber("blogId", Integer.class);

    public final StringPath blogKey = createString("blogKey");

    public final SimplePath<Object> blogMisc = createSimple("blogMisc", Object.class);

    public final StringPath blogName = createString("blogName");

    public final com.querydsl.sql.PrimaryKey<QBlog> blogPk = createPrimaryKey(blogId);

    public final com.querydsl.sql.ForeignKey<QBlogOwnership> _blogOwnershipBlogFk = createInvForeignKey(blogId, "blog_id");

    public final com.querydsl.sql.ForeignKey<QRequest> _requestBlogFk = createInvForeignKey(blogId, "blog_id");

    public QBlog(String variable) {
        super(QBlog.class, forVariable(variable), "replayka", "blog");
        addMetadata();
    }

    public QBlog(String variable, String schema, String table) {
        super(QBlog.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QBlog(String variable, String schema) {
        super(QBlog.class, forVariable(variable), schema, "blog");
        addMetadata();
    }

    public QBlog(Path<? extends QBlog> path) {
        super(path.getType(), path.getMetadata(), "replayka", "blog");
        addMetadata();
    }

    public QBlog(PathMetadata metadata) {
        super(QBlog.class, metadata, "replayka", "blog");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(blogId, ColumnMetadata.named("blog_id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(blogKey, ColumnMetadata.named("blog_key").withIndex(3).ofType(Types.VARCHAR).withSize(10).notNull());
        addMetadata(blogMisc, ColumnMetadata.named("blog_misc").withIndex(4).ofType(Types.OTHER).withSize(2147483647));
        addMetadata(blogName, ColumnMetadata.named("blog_name").withIndex(2).ofType(Types.VARCHAR).withSize(100).notNull());
    }

}

