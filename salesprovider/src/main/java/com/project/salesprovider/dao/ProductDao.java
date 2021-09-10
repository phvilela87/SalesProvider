package com.project.salesprovider.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class ProductDao {
	
	// Nome da minha tabela no cloud datastore
	public static final String PRODUCT_KIND = "Products";

	// Critérios de busca
	public static final String CRITERIA_ALL_ENTITIES = "CRITERIA_ALL_ENTITIES";
	public static final String CRITERIA_BY_ID = "CRITERIA_BY_ID";

	// Propriedadesda tabela que está no cloud datastore
	public static final String PROP_OBJ_ID = "objId";
	public static final String PROP_NAME = "name";
	public static final String PROP_DESCRIPTION = "description";
	public static final String PROP_CODE = "code";
	public static final String PROP_PRICE = "price";
	
	// Salva ou atualiza a entidade
	public Entity save(Entity entity) {
		try {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			datastore.put(entity);
		} catch (Exception e) {
			System.out.println(e);
		}
		return entity;
	}

	// Obtem a lista de entidades
	public List<Entity> getEntitiesByCriteria(Map<String, String> criteria) {
		List<Entity> entities = new ArrayList<Entity>();
		try {
			if(criteria == null || criteria.size() == 0){
				return null;
			}
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();

			if (criteria.containsKey(CRITERIA_ALL_ENTITIES)) {
				Query query = new Query(PRODUCT_KIND);
				entities = datastore.prepare(query).asList(
						FetchOptions.Builder.withDefaults());
			}
			if(entities == null || entities.size() == 0){
				return null;
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return entities;
	}

	// Obtem a entidade
	public Entity getEntityByKey(String key) {
		Entity entity = null;
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			Filter filter = new FilterPredicate(PROP_OBJ_ID, FilterOperator.EQUAL, key);

			Query query = new Query(PRODUCT_KIND).setFilter(filter);
			List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

			if(entities == null || entities.size() != 1){
				return null;
			}
			entity = entities.get(0);
		} catch (Exception e) {
			System.out.println(e);
		}
		return entity;
	}

	// Deleta a entidade
	public boolean deleteByKey(Key key) {
		boolean result = false;
		try {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			datastore.delete(key);
			result = true;
		} catch (Exception e) {
			System.out.println(e);
		}
		return result;
	}

}
