import com.ssheld.contactmgr.model.Contact;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;


/**
 * Author: Stephen Sheldon 3/25/2019
 */

public class Application {

    // Hold a reusable reference to a SessionFactory (since we need only one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        Contact contact = new Contact.ContactBuilder("Stephen","Sheldon")
                .withEmail("stephen@sheldon.com")
                .withPhone(7773334444L)
                .build();
        int id = save(contact);

        // Display a list of contacts before the update
        System.out.printf("%n%nBefore update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Get the persisted contact
        Contact c = findContactById(id);

        // Update the contact
        c.setFirstName("Jim");
        c.setLastName("Barry");
        // Persist the changes
        System.out.printf("%n%nUpdating...%n%n");
        update(c);
        System.out.printf("%n%nUpdating complete%n%n");
        // Display a list of contacts after the update
        System.out.printf("%n%nAfter update%n%n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Delete the contact
        System.out.printf("%nDeleting...%n");
        delete(c);
        System.out.printf("%nDeleted!%n");
        System.out.printf("%nAfter delete%n");
        fetchAllContacts().stream().forEach(System.out::println);
    }

    private static Contact findContactById(int id) {
        // Open a session
        Session session = sessionFactory.openSession();
        // Retrieve the persistent object (or null if not found)
        Contact contact = session.get(Contact.class, id);
        // Close the session
        session.close();
        // Return the object
        return contact;
    }

    private static void update(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();
        // Begin a transaction
        session.beginTransaction();
        // Use the session to update the contact
        session.update(contact);
        // Commit the transaction
        session.getTransaction().commit();;
        // Close the session
        session.close();
    }

    private static void delete(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();
        // Begin a transaction
        session.beginTransaction();
        // Use the session to delete the contact
        session.delete(contact);
        // Commit the transaction
        session.getTransaction().commit();
        // Close the session
        session.close();
    }

    private static int save(Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();
        // Begin a transaction
        session.beginTransaction();
        // Use the session to save the contact
        int id = (int)session.save(contact);
        // Commit the transaction
        session.getTransaction().commit();
        // Close the session
        session.close();

        return id;
    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        // Open a session
        Session session = sessionFactory.openSession();

        // Create a Criteria
        Criteria criteria = session.createCriteria(Contact.class);

        // Get a list of Contact objects according to the Criteria object
        List<Contact> contacts = criteria.list();

        // Close a session
        session.close();

        return contacts;
    }
}
