package me.mrabar.replayka;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QBlogOwnership is a Querydsl query type for QBlogOwnership
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QBlogOwnership extends com.querydsl.sql.RelationalPathBase<QBlogOwnership> {

    private static final long serialVersionUID = -761729780;

    public static final QBlogOwnership blogOwnership = new QBlogOwnership("blog_ownership");

    public final NumberPath<Integer> blogId = createNumber("blogId", Integer.class);

    public final NumberPath<Integer> loginId = createNumber("loginId", Integer.class);

    public final com.querydsl.sql.ForeignKey<QBlog> blogOwnershipBlogFk = createForeignKey(blogId, "blog_id");

    public final com.querydsl.sql.ForeignKey<QSocialLogin> blogOwnershipSocialLoginFk = createForeignKey(loginId, "id");

    public QBlogOwnership(String variable) {
        super(QBlogOwnership.class, forVariable(variable), "replayka", "blog_ownership");
        addMetadata();
    }

    public QBlogOwnership(String variable, String schema, String table) {
        super(QBlogOwnership.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QBlogOwnership(String variable, String schema) {
        super(QBlogOwnership.class, forVariable(variable), schema, "blog_ownership");
        addMetadata();
    }

    public QBlogOwnership(Path<? extends QBlogOwnership> path) {
        super(path.getType(), path.getMetadata(), "replayka", "blog_ownership");
        addMetadata();
    }

    public QBlogOwnership(PathMetadata metadata) {
        super(QBlogOwnership.class, metadata, "replayka", "blog_ownership");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(blogId, ColumnMetadata.named("blog_id").withIndex(2).ofType(Types.INTEGER).withSize(10));
        addMetadata(loginId, ColumnMetadata.named("login_id").withIndex(1).ofType(Types.INTEGER).withSize(10));
    }

}

