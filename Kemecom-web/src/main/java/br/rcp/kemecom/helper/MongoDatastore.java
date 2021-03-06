package br.rcp.kemecom.helper;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.validation.ValidationExtension;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 <p/>
 @author barenko
 */
public class MongoDatastore {

    private Logger log = LoggerFactory.getLogger(MongoDatastore.class);

    private final Datastore ds;

    private final MongoClient mongo;

    private final Morphia morphia;

    public MongoDatastore(String host, int port, String dbname, String username, String password) throws UnknownHostException {
        if(log.isInfoEnabled()){
            log.info("Carregando MongoDatastore para dbname=" + dbname + " ...");
        }

        mongo = new MongoClient(host, port);
        morphia = new Morphia();
        ds = morphia.createDatastore(mongo, dbname, username, password == null ? null : password.toCharArray());
        if(log.isInfoEnabled()){
            log.info("MongoDatastore carregado com sucesso!");
        }
    }

    public MongoDatastore enableValidation() {
        if(log.isInfoEnabled()){
            log.info("Validacao das entidades do MongoDB ativada!");
        }

        new ValidationExtension(morphia);
        return this;
    }

    public MongoDatastore registryEntity(Class<?>... entities) {
        if(log.isInfoEnabled()){
            log.info("Registrando as entidades: " + Arrays.toString(entities));
        }

        morphia.map(entities);
        ds.ensureIndexes();

        if(log.isInfoEnabled()){
            log.info("Entidades registradas com sucesso!");
        }

        return this;
    }

    public Datastore getDataStore() {
        return ds;
    }
}
