package com.project.salesprovider.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.project.salesprovider.dao.ProductDao;
import com.project.salesprovider.model.Product;

@Path("/products")
public class ProductService {

	private ProductDao dao = new ProductDao();

	// CONVERSAO ENTITY PARA PRODUCT
	public static Product entityToObject(Entity entity) {
		Product obj = new Product();
		try {
			obj.setId(entity.getKey().getId());
			obj.setObjId((String) entity.getProperty(ProductDao.PROP_OBJ_ID));
			obj.setName((String) entity.getProperty(ProductDao.PROP_NAME));
			obj.setDescription((String) entity.getProperty(ProductDao.PROP_DESCRIPTION));
			obj.setCode((String) entity.getProperty(ProductDao.PROP_CODE));
			obj.setPrice((double) entity.getProperty(ProductDao.PROP_PRICE));
		} catch (Exception e) {
			System.out.println(e);
		}
		return obj;
	}

	// CONVERSAO PRODUCT PARA ENTITY
	private void objectToEntity(Product obj, Entity entity) {
		entity.setProperty(ProductDao.PROP_OBJ_ID, obj.getObjId());
		entity.setProperty(ProductDao.PROP_NAME, obj.getName());
		entity.setProperty(ProductDao.PROP_DESCRIPTION, obj.getDescription());
		entity.setProperty(ProductDao.PROP_CODE, obj.getCode());
		entity.setProperty(ProductDao.PROP_PRICE, obj.getPrice());
	}

	// Obtem a lista de entidades
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get")
	public List<Product> getEntities() {
		List<Product> objects = new ArrayList<>();
		Map<String, String> criteria = new HashMap<String, String>();
		criteria.put(ProductDao.CRITERIA_ALL_ENTITIES, ProductDao.CRITERIA_ALL_ENTITIES);
		List<Entity> entities = dao.getEntitiesByCriteria(criteria);
		if (entities == null || entities.size() == 0) {
			return null;
		}
		for (Entity entity : entities) {
			Product obj = entityToObject(entity);
			objects.add(obj);
		}
		return objects;
	}

	// Adiciona a entidade
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/post")
	public Response saveEntity(@Valid Product obj) {

		try {
			// Se o objeto nao vier com id, prepara para inserir no banco de
			// dados
			if (obj.getId() == 0) {
				Key productKey = KeyFactory.createKey(ProductDao.PRODUCT_KIND, "productKey");
				Entity entity = new Entity(ProductDao.PRODUCT_KIND, productKey);

				// Gera a string unica
				String objId = UUID.randomUUID().toString();
				obj.setObjId(objId);

				objectToEntity(obj, entity);
				dao.save(entity);
				obj = entityToObject(entity);
			}
		} catch (Exception e) {
			System.out.println(e);
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.status(Status.OK).entity(obj).build();
	}

	// Atualiza a entidade
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/update")
	public Response updateEntity(@Valid Product obj) {

		Entity entity = dao.getEntityByKey(obj.getObjId());
		if (entity == null) {
			return null;
		} else {
			objectToEntity(obj, entity);
			dao.save(entity);
			obj = entityToObject(entity);
		}
		return Response.status(Status.OK).entity(obj).build();
	}

	// Deleta a entidade
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/delete/")
	public Response delete(Product obj) {
		try {
			if (obj.getId() == 0) {
				return Response.status(Status.BAD_REQUEST).build();
			}

			Entity entity = dao.getEntityByKey(obj.getObjId());
			objectToEntity(obj, entity);

			if (entity != null) {
				dao.deleteByKey(entity.getKey());
			}

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		return Response.status(Status.OK).build();
	}

}
