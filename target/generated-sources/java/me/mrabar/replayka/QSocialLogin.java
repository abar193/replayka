package me.mrabar.replayka;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QSocialLogin is a Querydsl query type for QSocialLogin
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QSocialLogin extends com.querydsl.sql.RelationalPathBase<QSocialLogin> {

    private static final long serialVersionUID = 382929627;

    public static final QSocialLogin socialLogin = new QSocialLogin("social_login");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath source = createString("source");

    public final com.querydsl.sql.PrimaryKey<QSocialLogin> socialLoginPk = createPrimaryKey(id);

    public final com.querydsl.sql.ForeignKey<QBlogOwnership> _blogOwnershipSocialLoginFk = createInvForeignKey(id, "login_id");

    public QSocialLogin(String variable) {
        super(QSocialLogin.class, forVariable(variable), "replayka", "social_login");
        addMetadata();
    }

    public QSocialLogin(String variable, String schema, String table) {
        super(QSocialLogin.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSocialLogin(String variable, String schema) {
        super(QSocialLogin.class, forVariable(variable), schema, "social_login");
        addMetadata();
    }

    public QSocialLogin(Path<? extends QSocialLogin> path) {
        super(path.getType(), path.getMetadata(), "replayka", "social_login");
        addMetadata();
    }

    public QSocialLogin(PathMetadata metadata) {
        super(QSocialLogin.class, metadata, "replayka", "social_login");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128));
        addMetadata(source, ColumnMetadata.named("source").withIndex(3).ofType(Types.VARCHAR).withSize(20));
    }

}

