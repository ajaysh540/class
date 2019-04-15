public class AdminDetailsService implements IAirport {

	public AdminDetailsService() {
		// TODO Auto-generated constructor stub
	}

	
	@Autowired
	IAirport airportDAO;


	@Override
	public AdminDetails adminreg(AdminDetails ad) {
		return airportDAO.adminreg(ad);
	}


	@Override
	public List<AdminDetails> adminreqList() {
		// TODO Auto-generated method stub
		return airportDAO.adminreqList();
	}


	@Override
	public void adminapproverequest(int id) {
		// TODO Auto-generated method stub
		airportDAO.adminapproverequest(id);
	}


	@Override
	public List<ManagerDetails> managerreqList() {
		// TODO Auto-generated method stub
		return airportDAO.managerreqList();
	}


	@Override
	public void managerapproverequest(int id) {
		// TODO Auto-generated method stub
		airportDAO.managerapproverequest(id);
	}


	@Override
	public String adminLogin(LoginDetails loginDetails) {
		// TODO Auto-generated method stub
		return airportDAO.adminLogin(loginDetails);
	}


	@Override
	public AdminDetails getAdminDetails(int userId) {
		// TODO Auto-generated method stub
		return airportDAO.getAdminDetails(userId);
	}




	
	
	

}
public class AdminDao implements IAirport {

	private AdminDao() {
		// TODO Auto-generated constructor stub
	}

	@PersistenceContext
	public EntityManager entityManager;

	@Transactional
	@Override
	public AdminDetails adminreg(AdminDetails ad) {
		// TODO Auto-generated method stub
		AdminDetails adminDetails=entityManager.merge(ad);
		System.out.println(adminDetails.toString());
		return adminDetails;
	}

	@Override
	public List<AdminDetails> adminreqList() {
		// TODO Auto-generated method stub
		List<AdminDetails> li = null;
		String hql = "Select l from AdminDetails l where l.status=?";
		TypedQuery<AdminDetails> query = entityManager.createQuery(hql, AdminDetails.class);
		query.setParameter(0, 0);
		li = query.getResultList();

		return li;
	}

	@Override
	public List<ManagerDetails> managerreqList() {
		// TODO Auto-generated method stub
		List<ManagerDetails> li = null;
		String hql = "Select l from ManagerDetails l where l.status=?";
		TypedQuery<ManagerDetails> query = entityManager.createQuery(hql, ManagerDetails.class);
		query.setParameter(0, 0);
		li = query.getResultList();

		return li;
	}

	@Transactional
	@Override
	public void adminapproverequest(int id) {
		AdminDetails details = entityManager.find(AdminDetails.class, id);
		details.setStatus(1);
		entityManager.merge(details);
	}

	@Transactional
	@Override
	public void managerapproverequest(int id) {
		// TODO Auto-generated method stub
		ManagerDetails details = entityManager.find(ManagerDetails.class, id);
		details.setStatus(1);
		entityManager.merge(details);
	}

	@Override
	public String adminLogin(LoginDetails loginDetails) {
		AdminDetails adminDetails = entityManager.find(AdminDetails.class, LoginDetails.getUserId());

		if (adminDetails != null) {
			if (adminDetails.getStatus() == 0) {
				System.out.println("Account Not Activated.");
				return "Account Not Activated.";
			}

			if (adminDetails.getPassword().equals(LoginDetails.getPassword())) {
				System.out.println(adminDetails.toString());
				return "success";
			} else {
				System.out.println("Wrong Password");
				return ("Wrong Password");
			}
		}

		else {
			System.out.println("Wrong UserId");
			return "Wrong UserId";
		}
	}

	@Override
	public AdminDetails getAdminDetails(int userId) {

		return entityManager.find(AdminDetails.class, userId);
	}

	



}
public class AdminDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private Integer adminId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "age")
	private int age;

	@Column(name = "gender")
	private String gender;

	@Column(name = "dob")
	private String dob;

	@Column(name = "contact")
	private int contact;

	@Column(name = "alt_contact")
	private int altContact;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "status")
	private int status;

	@Override
	public String toString() {
		return "AdminDetails [adminId=" + adminId + ", firstName=" + firstName + ", lastName=" + lastName + ", age="
				+ age + ", gender=" + gender + ", dob=" + dob + ", contact=" + contact + ", altContact=" + altContact
				+ ", email=" + email + ", pass=" + password + ", status=" + status + "]";
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public int getContact() {
		return contact;
	}

	public void setContact(int contact) {
		this.contact = contact;
	}

	public int getAltContact() {
		return altContact;
	}

	public void setAltContact(int altContact) {
		this.altContact = altContact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
public class AirportController {

	public AirportController() {

	}

	@Resource(name = "adminService")
	IAirport adminService;

	@RequestMapping(value = "/RequestList", method = RequestMethod.GET)
	public ModelAndView reqlist() // to display the unapproved admins and managers
	{
		ModelAndView modelAndView = new ModelAndView("pending");
		List<AdminDetails> ad = adminService.adminreqList();
		modelAndView.addObject("adminList", ad);
		List<ManagerDetails> md = adminService.managerreqList();
		modelAndView.addObject("managerList", md);
		return modelAndView;
	}

	@RequestMapping(value = "/AdminApproveRequest", method = RequestMethod.POST)
	public String adminapprovereq(Integer adminid) // to approve the another admin
	{
		adminService.adminapproverequest(adminid);
		return "redirect:/app/RequestList";

	}

	@RequestMapping(value = "/ManagerApproveRequest", method = RequestMethod.POST)
	public String managerapprovereq(Integer managerid) // to approve the manager
	{
		// System.out.println(managerid);
		adminService.managerapproverequest(managerid);
		return "redirect:/app/RequestList";
	}
}
public class AirportManagementSystemAddPlaneService implements IAddPlane {

	@Autowired
	AddPlaneDao addPlaneDao;
	
	@Transactional
	@Override
	public boolean addPlane(PlaneDetails planeDetails) {
		// TODO Auto-generated method stub
		return addPlaneDao.addPlane(planeDetails);
	}

	@Override
	public List<PlaneDetails> viewAllPlanes() {
		// TODO Auto-generated method stub
		return addPlaneDao.viewAllPlanes();
	}
	@Transactional
	@Override
	public void updatePlane(PlaneDetails planeDetails) {
		addPlaneDao.updatePlane(planeDetails);
	}

	@Override
	public PlaneDetails viewDetails(Integer pid) {
		// TODO Auto-generated method stub
		return addPlaneDao.viewDetails(pid);
	}

}
public class AssignHangarPlane {

	@Resource(name="assignHangarPlaneService")
	IAssignHangarPlane assignHangarPlane;
	
	public AssignHangarPlane() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/ViewUnassignedHangarPlane", method=RequestMethod.GET)
	public ModelAndView viewUnssigned() {
		ModelAndView modelAndView = new ModelAndView("hangarPlane");
		List<PlaneDetails> planeList = assignHangarPlane.getUnassignedPlane();
		List<HangarStatus> hangarList = assignHangarPlane.getUnassignedHangar();
		modelAndView.addObject("planeList",planeList);
		modelAndView.addObject("hangarList",hangarList);
		return modelAndView;
	}
	
	@RequestMapping(value="/AssignHangarPlane", method=RequestMethod.POST)
	public String assignPlaneToHangar(String hangarId, String planeId, String availableFD, String availableTD, String occupancyFD, String occupancyTD) {
		PlaneHangarStatus planeHangarStatus = new PlaneHangarStatus();
		HangarDetails hangarDetails = assignHangarPlane.findHangarDetailsById(Integer.parseInt(hangarId));
		PlaneDetails planeDetails = assignHangarPlane.findPlaneDetailsById(Integer.parseInt(planeId));
		planeHangarStatus.setHangarDetails(hangarDetails);
		planeHangarStatus.setPlaneDetails(planeDetails);
		assignHangarPlane.assignHangarPlane(planeHangarStatus);
		assignHangarPlane.updateHangarStatusDetails(Integer.parseInt(hangarId),availableFD,availableTD,occupancyFD,occupancyTD);
		return "redirect:/app/checkhangarstatus";
		//return null;
	}
}
public class AssignHangarPlaneDao implements IAssignHangarPlane {

	@PersistenceContext
	private EntityManager entityManager;
	
	private static AssignHangarPlaneDao assignHangarPlaneDao = null;
	private AssignHangarPlaneDao() {
		// TODO Auto-generated constructor stub
	}
	public static AssignHangarPlaneDao getAssignHangarPlaneDao() {
		if(assignHangarPlaneDao == null)
			return assignHangarPlaneDao;
		return new AssignHangarPlaneDao();
	}
	
	@Override
	public List<PlaneDetails> getUnassignedPlane() {
		// TODO Auto-generated method stub
		//String hql="SELECT p.planeId FROM PlaneDetails p WHERE p.planeId NOT IN (SELECT phd.planes_plane_id FROM PlaneHangarStatus phd) AND (SELECT ppd.planes_plane_id FROM PilotPlaneDetails ppd)";
		String hql="SELECT pd FROM PlaneDetails pd WHERE pd.planeId NOT IN (SELECT phd.planeDetails FROM PlaneHangarStatus phd) AND pd.planeId NOT IN (SELECT ppd.planeDetails FROM PilotPlaneDetails ppd)";
		TypedQuery<PlaneDetails> query = entityManager.createQuery(hql,PlaneDetails.class);
		List<PlaneDetails> list = query.getResultList();
		return list;
	}
	
	@Transactional
	@Override
	public boolean assignHangarPlane(PlaneHangarStatus planeHangarStatus) {
		// TODO Auto-generated method stub
		entityManager.persist(planeHangarStatus);
		return true;
	}

	@Override
	public List<HangarStatus> getUnassignedHangar() {
		// TODO Auto-generated method stub
		String hql="SELECT h FROM HangarStatus h WHERE h.status='A'";
		TypedQuery<HangarStatus> query = entityManager.createQuery(hql,HangarStatus.class);
		List<HangarStatus> list = query.getResultList();
		return list;
	}
	@Override
	public HangarDetails findHangarDetailsById(int hangarId) {
		// TODO Auto-generated method stub
		String hql="SELECT h FROM HangarDetails h WHERE h.hangarId="+hangarId;
		TypedQuery<HangarDetails> query = entityManager.createQuery(hql,HangarDetails.class);
		HangarDetails hangarStatus = query.getSingleResult();
		return hangarStatus;
	}
	@Override
	public PlaneDetails findPlaneDetailsById(int planeId) {
		// TODO Auto-generated method stub
		PlaneDetails planeDetails = entityManager.find(PlaneDetails.class, planeId);
		return planeDetails;
	}
	@Transactional
	@Override
	public boolean updateHangarStatusDetails(int hangarId, String availableFD, String availableTD, String occupancyFD, String occupancyTD) {
		// TODO Auto-generated method stub
		String hql = "SELECT hs FROM HangarStatus hs WHERE hs.hangarDetails.hangarId="+hangarId;
		TypedQuery<HangarStatus> query = entityManager.createQuery(hql,HangarStatus.class);
		HangarStatus hangarStatus = query.getSingleResult();
		hangarStatus.setStatus("N");
		hangarStatus.setAvailableFD(availableFD);
		hangarStatus.setAvailableTD(availableTD);
		hangarStatus.setOccupancyFD(occupancyFD);
		hangarStatus.setOccupancyTD(occupancyTD);
		entityManager.merge(hangarStatus);
		return true;
	}

}
public class AssignHangarPlaneService implements IAssignHangarPlane {

	@Autowired
	IAssignHangarPlane assignHangarPlane;
	
	@Override
	public boolean assignHangarPlane(PlaneHangarStatus planeHangarStatus) {
		// TODO Auto-generated method stub
		return assignHangarPlane.assignHangarPlane(planeHangarStatus);
	}

	@Override
	public List<PlaneDetails> getUnassignedPlane() {
		// TODO Auto-generated method stub
		return assignHangarPlane.getUnassignedPlane();
	}

	@Override
	public List<HangarStatus> getUnassignedHangar() {
		// TODO Auto-generated method stub
		return assignHangarPlane.getUnassignedHangar();
	}

	@Override
	public HangarDetails findHangarDetailsById(int hangarId) {
		// TODO Auto-generated method stub
		return assignHangarPlane.findHangarDetailsById(hangarId);
	}

	@Override
	public PlaneDetails findPlaneDetailsById(int planeId) {
		// TODO Auto-generated method stub
		return assignHangarPlane.findPlaneDetailsById(planeId);
	}

	@Override
	public boolean updateHangarStatusDetails(int hangarId, String availableFD, String availableTD, String occupancyFD, String occupancyTD) {
		// TODO Auto-generated method stub
		return assignHangarPlane.updateHangarStatusDetails(hangarId, availableFD, availableTD, occupancyFD, occupancyTD);
	}
	
}
public class AssignPlanePilot {
	
	@Resource(name="assignPlanePilotService")
	IAssignPlanePilot assignPlanePilot;
	
	@RequestMapping(value="/ViewUnassignedPlaneHangar", method=RequestMethod.POST)
	public ModelAndView viewUnssigned(PlaneHangarStatus planeHangarStatus, PilotPlaneDetails pilotPlaneDetails, PlaneDetails planeDetails, HangarStatus hangarStatus, PilotDetails pilotDetails) {
		ModelAndView modelAndView = new ModelAndView("assignPilotPlane");
		List<PlaneDetails> planeList = assignPlanePilot.getUnassignedPlane();
		List<PilotDetails> pilotList = assignPlanePilot.getUnassignedPilot();
		modelAndView.addObject("planeList",planeList);
		modelAndView.addObject("pilotList",pilotList);
		return modelAndView;
	}
	
	@RequestMapping(value="/AssignPilotPlane", method=RequestMethod.POST)
	public ModelAndView assignPlaneToPilot(String planeId, String pilotId) {
		ModelAndView modelAndView = new ModelAndView("Success");	// JSP name
		modelAndView.addObject("message","Assigned successfully"); //attribute value
		PilotPlaneDetails pilotPlaneDetails = new PilotPlaneDetails();
		PilotDetails pilotDetails = assignPlanePilot.findPilotDetailsById(Integer.parseInt(pilotId));
		PlaneDetails planeDetails = assignPlanePilot.findPlaneDetailsById(Integer.parseInt(planeId));
		pilotPlaneDetails.setPilotDetails(pilotDetails);
		pilotPlaneDetails.setPlaneDetails(planeDetails);
		assignPlanePilot.assignPlaneToPilot(pilotPlaneDetails);
		return modelAndView;
	}

}
public class AssignPlanePilotDao implements IAssignPlanePilot{

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<PlaneDetails> getUnassignedPlane() {
		// TODO Auto-generated method stub
		//String hql="SELECT p.planeId FROM PlaneDetails p WHERE p.planeId NOT IN (SELECT phd.planes_plane_id FROM PlaneHangarStatus phd) AND (SELECT ppd.planes_plane_id FROM PilotPlaneDetails ppd)";
		String hql="SELECT p FROM PlaneDetails p WHERE p.planeId NOT IN (SELECT phd.planeDetails FROM PlaneHangarStatus phd) AND p.planeId NOT IN (SELECT ppd.planeDetails FROM PilotPlaneDetails ppd)";
		TypedQuery<PlaneDetails> query = entityManager.createQuery(hql,PlaneDetails.class);
		List<PlaneDetails> list = query.getResultList();
		return list;
	}

	@Override
	public List<PilotDetails> getUnassignedPilot() {
		// TODO Auto-generated method stub
		List<PilotDetails> list = null;
		//String hql="SELECT p.PilotId FROM PilotDetails p WHERE p.PilotId NOT IN (SELECT ppd.pilots_plane_id FROM PilotPlaneDetails ppd)";
		String hql="SELECT p FROM PilotDetails p WHERE p.pilotId NOT IN (SELECT ppd.pilotDetails FROM PilotPlaneDetails ppd)";
		TypedQuery<PilotDetails> query = entityManager.createQuery(hql, PilotDetails.class);
		list = query.getResultList();
		return list;
	}

	@Transactional
	@Override
	public boolean assignPlaneToPilot(PilotPlaneDetails pilotPlaneDetails) {
		// TODO Auto-generated method stub
		entityManager.merge(pilotPlaneDetails);
		return true;
	}

	@Override
	public PilotDetails findPilotDetailsById(int pilotId) {
		// TODO Auto-generated method stub
		PilotDetails pilotDetails = entityManager.find(PilotDetails.class, pilotId);
		return pilotDetails;
	}

	@Override
	public PlaneDetails findPlaneDetailsById(int planeId) {
		// TODO Auto-generated method stub
		PlaneDetails planeDetails = entityManager.find(PlaneDetails.class, planeId);
		return planeDetails;
	}

}
@Service(value="assignPlanePilotService")
public class AssignPlanePilotService implements IAssignPlanePilot {

	@Autowired
	IAssignPlanePilot assignPlanePilot;
	
	@Override
	public List<PlaneDetails> getUnassignedPlane() {
		// TODO Auto-generated method stub
		return assignPlanePilot.getUnassignedPlane();
	}

	@Override
	public List<PilotDetails> getUnassignedPilot() {
		// TODO Auto-generated method stub
		return assignPlanePilot.getUnassignedPilot();
	}

	@Override
	public boolean assignPlaneToPilot(PilotPlaneDetails pilotPlaneDetails) {
		// TODO Auto-generated method stub
		return assignPlanePilot.assignPlaneToPilot(pilotPlaneDetails);
	}

	@Override
	public PilotDetails findPilotDetailsById(int pilotId) {
		// TODO Auto-generated method stub
		return assignPlanePilot.findPilotDetailsById(pilotId);
	}

	@Override
	public PlaneDetails findPlaneDetailsById(int planeId) {
		// TODO Auto-generated method stub
		return assignPlanePilot.findPlaneDetailsById(planeId);
	}

}
public class HangarController {

	static Logger logger = Logger.getLogger(HangarController.class);

	public HangarController() {
		// TODO Auto-generated constructor stub
	}

	@Resource(name = "HangarHibService")
	IHangarDao HangarHibService;

	@RequestMapping(value = "/addHangar", method = RequestMethod.GET)
	public String addPage() {
		return "addHangar";
	}

	@RequestMapping(value = "/addhangardetails", method = RequestMethod.POST)
	public String addhangdetails(HangarDetails hd) {
		HangarHibService.addHangarDetails(hd);
		return "redirect:/app/viewAllHangars";
	}
	

	@RequestMapping(value = "/viewAllHangars", method = RequestMethod.GET)
	public ModelAndView added(HangarDetails hd) {
		ModelAndView modelAndView = new ModelAndView("viewHangar");

		List<HangarDetails> hd1 = HangarHibService.getAllHangar();

		//modelAndView = new ModelAndView(/* "jsp name" */);
		modelAndView.addObject("hangarList", hd1);

		return modelAndView;
	}

	@RequestMapping(value = "/viewhanger", method = RequestMethod.GET)
	public ModelAndView viewhanger(int hId) {
		
		ModelAndView modelAndView = new ModelAndView("editHangar");
		modelAndView.addObject("hangar", HangarHibService.getHangarDetails(hId));
		return modelAndView;
	}
	@RequestMapping(value="/edithangar",method=RequestMethod.POST)
	public String editHangar(HangarDetails hangarDetails) {
		HangarHibService.editHangarDetails(hangarDetails);
		return "redirect:/app/viewAllHangars";
	}

}
public class HangarDao implements IHangarDao {

	static Logger logger = Logger.getLogger(HangarDao.class);

	public HangarDao() {
		// TODO Auto-generated constructor stub
	}

	@PersistenceContext
	public EntityManager entityManager;

	@Transactional
	@Override
	public void addHangarDetails(HangarDetails hd) {
		// TODO Auto-generated method stub

		logger.info(hd);
		entityManager.merge(hd);
	}

	@Transactional
	@Override
	public void addHangarStatus(HangarStatus hs) {
		// TODO Auto-generated method stub
		logger.info(hs);
		entityManager.merge(hs);
	}

	@Override
	public List<HangarDetails> getAllHangar() {
		// TODO Auto-generated method stub
		logger.info("HangarDetails getProduct");

		List<HangarDetails> li = null;
		String hql = "select l from HangarDetails l";
		TypedQuery<HangarDetails> query = entityManager.createQuery(hql, HangarDetails.class);
		li = query.getResultList();
		return li;
	}

	@Override
	public HangarDetails getHangarDetails(int hId) {
		return entityManager.find(HangarDetails.class, hId);
	}

	@Override
	public void editHangarDetails(HangarDetails hangarDetails) {
		entityManager.merge(hangarDetails);
		
	}

}
public class HangarDetails {

	public HangarDetails() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@Column(name="hangar_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int hangarId;
	
	
	@Column(name="manager_id")
	private String managerId;
	
	@Column(name="manager_address1")
	private String managerAddress1;

	@Column(name="manager_address2")
	private String managerAddress2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="zipcode")
	private String zipCode;

	public int getHangarId() {
		return hangarId;
	}

	public void setHangarId(int hangarId) {
		this.hangarId = hangarId;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getManagerAddress1() {
		return managerAddress1;
	}

	public void setManagerAddress1(String managerAddress1) {
		this.managerAddress1 = managerAddress1;
	}

	public String getManagerAddress2() {
		return managerAddress2;
	}

	public void setManagerAddress2(String managerAddress2) {
		this.managerAddress2 = managerAddress2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return "HangarDetails [hangarId=" + hangarId + ", managerId=" + managerId + ", managerAddress1="
				+ managerAddress1 + ", managerAddress2=" + managerAddress2 + ", city=" + city + ", state=" + state
				+ ", zipCode=" + zipCode + "]";
	}
	
	
}
public class HangarHibService implements IHangarDao {

	public HangarHibService() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	IHangarDao hangarDao;

	@Override
	public void addHangarDetails(HangarDetails hd) {
		// TODO Auto-generated method stub
		hangarDao.addHangarDetails(hd);
	}

	@Override
	public void addHangarStatus(HangarStatus hs) {
		// TODO Auto-generated method stub
		hangarDao.addHangarStatus(hs);
	}

	@Override
	public List<HangarDetails> getAllHangar() {
		// TODO Auto-generated method stub
		return hangarDao.getAllHangar();
	}

	@Override
	public HangarDetails getHangarDetails(int hId) {
		// TODO Auto-generated method stub
		return hangarDao.getHangarDetails(hId);
	}

	@Transactional
	@Override
	public void editHangarDetails(HangarDetails hangarDetails) {
		// TODO Auto-generated method stub
		hangarDao.editHangarDetails(hangarDetails);
	}

}
public class HangarStatus {

	public HangarStatus() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "HangarStatus [hangarDetails=" + hangarDetails + ", managerid=" + managerid + ", status=" + status
				+ ", occupancyFD=" + occupancyFD + ", occupancyTD=" + occupancyTD + ", availableFD=" + availableFD
				+ ", availableTD=" + availableTD + "]";
	}

	@OneToOne(fetch=FetchType.EAGER ,cascade=CascadeType.ALL)
    @JoinColumn(name="hangar_id")
	HangarDetails hangarDetails;

	
   /* @ManyToOne(fetch=FetchType.EAGER ,cascade=CascadeType.ALL)
    @JoinColumn(name="pr_br_fk")
       ProductBrands productBrands;*/
	@Id
	@Column(name="s_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@Column(name="manager_id")
	private String managerid;
	
	
	@Column(name="status")
	private String status;
	
	@Column(name="occupancy_from_date")
	private String occupancyFD;
	
	
	@Column(name="occupancy_till_date")
	private String occupancyTD;
	
	@Column(name="available_from_date")
	private String availableFD;
	
	@Column(name="available_till_date")
	private String availableTD;

	public HangarDetails getHangarDetails() {
		return hangarDetails;
	}

	public void setHangarDetails(HangarDetails hangarDetails) {
		this.hangarDetails = hangarDetails;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOccupancyFD() {
		return occupancyFD;
	}

	public void setOccupancyFD(String occupancyFD) {
		this.occupancyFD = occupancyFD;
	}

	public String getOccupancyTD() {
		return occupancyTD;
	}

	public void setOccupancyTD(String occupancyTD) {
		this.occupancyTD = occupancyTD;
	}

	public String getAvailableFD() {
		return availableFD;
	}

	public void setAvailableFD(String availableFD) {
		this.availableFD = availableFD;
	}

	public String getAvailableTD() {
		return availableTD;
	}

	public void setAvailableTD(String availableTD) {
		this.availableTD = availableTD;
	}
}
public interface IAddPlane {
	public boolean addPlane(PlaneDetails planeDetails);

	public List<PlaneDetails> viewAllPlanes();
	public void updatePlane(PlaneDetails planeDetails);

	public PlaneDetails viewDetails(Integer pid);
}
public interface IAirport {
	public AdminDetails adminreg(AdminDetails ad);

	public List<AdminDetails> adminreqList();

	public List<ManagerDetails> managerreqList();

	public void adminapproverequest(int id);

	public void managerapproverequest(int id);

	public String adminLogin(LoginDetails loginDetails);

	public AdminDetails getAdminDetails(int userId);

	
}
public interface IAssignHangarPlane {
	public boolean assignHangarPlane(PlaneHangarStatus planeHangarStatus);

	public List<PlaneDetails> getUnassignedPlane();

	public List<HangarStatus> getUnassignedHangar();
	
	public HangarDetails findHangarDetailsById(int hangarId);
	
	public PlaneDetails findPlaneDetailsById(int planeId);
	
	public boolean updateHangarStatusDetails(int hangarId, String availableFD, String availableTD, String occupancyFD, String occupancyTD);
}
public interface IAssignPlanePilot {

	public List<PlaneDetails> getUnassignedPlane();

	public List<PilotDetails> getUnassignedPilot();
	
	public boolean assignPlaneToPilot(PilotPlaneDetails pilotPlaneDetails);
	
	public PilotDetails findPilotDetailsById(int pilotId);
	
	public PlaneDetails findPlaneDetailsById(int planeId);
}
public interface IHangarDao {

	
	public HangarDetails getHangarDetails(int hId);
	public  void addHangarDetails(HangarDetails hd);
	public  void addHangarStatus(HangarStatus hs);
	public List<HangarDetails> getAllHangar();
	public void editHangarDetails(HangarDetails hangarDetails);
}
public interface IManagerDetailsService {
	public ManagerDetails saveManagerDetails(ManagerDetails managerDetails);
	public String managerLogin(LoginDetails loginDetails);
	public ManagerDetails getManagerDetails(int i);
}
public interface IPilotDao {
	public int addPilotDetails(PilotDetails pilotDetails);

	public int removePilotDetails(int pid);

	public List<PilotDetails> pilotreqList();

	public PilotDetails editPilotDetails(int id);

	public void saveEditPilotDetails(PilotDetails pd);

	public PilotDetails getPilotDetails(int pid);
}
public interface IUnassignPlane {
	public List<HangarStatus> getAssignedHangar();
	public void unassignHangar(String hangarId);
}
public class LoginControl {
	@Resource(name = "managerDetailsService")
	IManagerDetailsService managerDetailsService;

	@Resource(name = "adminService")
	IAirport airportService;

	ModelAndView modelAndView;

	@RequestMapping(value = "/login")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/Register")
	public String register(String submit) {
		if (submit.equals("Register-Manager"))
			return "RegisterManager";
		else
			return "RegisterAdmin";
	}

	@RequestMapping(value = "/AdminRegister", method = RequestMethod.POST)
	public ModelAndView adminreg(AdminDetails ad) // for storing admin details in the database
	{
		ModelAndView modelAndView = new ModelAndView("Success");
		AdminDetails adminDetails = airportService.adminreg(ad);
		modelAndView.addObject("Message", adminDetails.getAdminId());
		return modelAndView;
	}

	@RequestMapping(value = "/ManagerRegister", method = RequestMethod.POST)
	public ModelAndView RegisterManager(ManagerDetails managerDetails) {
		// System.out.println(managerDetails);
		ManagerDetails md = managerDetailsService.saveManagerDetails(managerDetails);
		modelAndView = new ModelAndView("Success");
		modelAndView.addObject("Message", md.getManagerId());
		// System.out.println("\n\n\n\\n\n\n\nn\n\n\\nn\n\\n\naaaaaaa");
		return modelAndView;

	}

	@RequestMapping(value = "/Login", method = RequestMethod.POST)
	public ModelAndView UserLogin(String Profile, Integer userId, String password, HttpServletRequest request,
			HttpServletResponse response) {
		LoginDetails.setProfile(Profile);
		LoginDetails.setUserId(userId);
		LoginDetails.setPassword(password);
		HttpSession session = request.getSession();

		if (LoginDetails.getProfile().equalsIgnoreCase("manager")) {
			String s = managerDetailsService.managerLogin(LoginDetails.getLoginDetails());
			if (s.equals("success")) {
				System.out.println("\n\n\n\nSuccess");
				modelAndView = new ModelAndView("ManagerAirport");
				ManagerDetails md = managerDetailsService.getManagerDetails(LoginDetails.getUserId());
				modelAndView.addObject("Manager", md);
				session.setAttribute("Name", md.getFirstName());
			} else {
				modelAndView = new ModelAndView("index");
				modelAndView.addObject("error", s);
			}
		} else {
			String s = airportService.adminLogin(LoginDetails.getLoginDetails());
			if (s.equals("success")) {
				System.out.println("\n\n\n\nSuccess");
				modelAndView = new ModelAndView("AdminAirport");
				AdminDetails ad = airportService.getAdminDetails(LoginDetails.getUserId());
				modelAndView.addObject("Admin", ad);
				session.setAttribute("Name", ad.getFirstName());
			} else {
				modelAndView = new ModelAndView("index");
				modelAndView.addObject("error", s);
			}
		}

		return modelAndView;
	}

	@RequestMapping(value = "/Home", method = RequestMethod.GET)
	public ModelAndView home() {
		System.out.println(LoginDetails.getUserId());
		if (LoginDetails.getProfile().equalsIgnoreCase("manager")) {
			String s = managerDetailsService.managerLogin(LoginDetails.getLoginDetails());
			if (s.equals("success")) {
				System.out.println("\n\n\n\nSuccess");
				modelAndView = new ModelAndView("ManagerAirport");
				ManagerDetails md = managerDetailsService.getManagerDetails(LoginDetails.getUserId());
				modelAndView.addObject("Manager", md);

			} else {
				modelAndView = new ModelAndView("index");
				modelAndView.addObject("error", s);
			}
		} else {
			String s = airportService.adminLogin(LoginDetails.getLoginDetails());
			if (s.equals("success")) {
				System.out.println("\n\n\n\nSuccess");
				modelAndView = new ModelAndView("AdminAirport");
				AdminDetails ad = airportService.getAdminDetails(LoginDetails.getUserId());
				modelAndView.addObject("Admin", ad);

			} else {
				modelAndView = new ModelAndView("index");
				modelAndView.addObject("error", s);
			}
		}
		return modelAndView;
	}

	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String logout(ModelAndView modelAndView, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "redirect:/app/login";
	}
	
	@RequestMapping(value = "/error", method = RequestMethod.POST)
	public ModelAndView error(ModelAndView modelAndView, HttpServletRequest request) {
		modelAndView=new ModelAndView("index");
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		modelAndView.addObject("error", "SOME ERROR OCCURED PLEASE LOGIN AGAIN");
		return modelAndView;
	}
}
public class LoginDetails {
	private static int userId;
	private static String password;
	private static String profile;
	private LoginDetails() {
		// TODO Auto-generated constructor stub
	}
	
	static private LoginDetails loginDetails;
	
	public static LoginDetails getLoginDetails() {
		if(loginDetails==null) loginDetails=new LoginDetails();
		return loginDetails;
	}
	
	
	
	@Override
	public String toString() {
		return "LoginDetails [userId=" + userId + ", password=" + password + ", profile=" + profile + "]";
	}

	public static int getUserId() {
		return userId;
	}

	public static void setUserId(int userId) {
		LoginDetails.userId = userId;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		LoginDetails.password = password;
	}

	public static String getProfile() {
		return profile;
	}

	public static void setProfile(String profile) {
		LoginDetails.profile = profile;
	}

	
	

}
@Repository
public class ManagerDao implements IManagerDetailsService {
	@PersistenceContext
	public EntityManager entityManager;
	

	@Override
	public ManagerDetails saveManagerDetails(ManagerDetails managerDetails) {

		ManagerDetails managerDetail=entityManager.merge(managerDetails);
		return managerDetail;

	}

	@Override
	public String managerLogin(LoginDetails loginDetails) {
		ManagerDetails managerDetails = entityManager.find(ManagerDetails.class,LoginDetails.getUserId());
		System.out.println(managerDetails);
		if(managerDetails!=null) {
			if(managerDetails.getStatus()==0) { System.out.println("Account Not Activated.");	return "Account Not Activated.";}
			
		if(managerDetails.getPassword().equals(LoginDetails.getPassword())) {
			System.out.println(managerDetails.toString());
			return "success";
		}
		else {
			System.out.println("Wrong Password");
			return ("Wrong Password");
		}}
		
		else {
			System.out.println("Wrong UserId");
		return "Wrong UserId";
		}
	}

	@Override
	public ManagerDetails getManagerDetails(int i) {
		// TODO Auto-generated method stub
		return entityManager.find(ManagerDetails.class,i);
	}
}
public class ManagerDetails {

	private ManagerDetails() {

	}

	private static ManagerDetails managerDetails;

	public static ManagerDetails getManagerDetails() {
		if (managerDetails == null) {
			managerDetails = new ManagerDetails();
		}
		return managerDetails;
	}

	@Override
	public String toString() {
		return "ManagerDetails [managerId=" + managerId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", age=" + age + ", gender=" + gender + ", dob=" + dob + ", contact=" + contact + ", altcontact="
				+ altContact + ", email=" + email + ", password=" + password + ", status=" + status + "]";
	}

	@Id
	@Column(name = "manager_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int managerId;
	
	@Column(name = "first_name")
	String firstName;
	
	@Column(name = "last_name")
	String lastName;
	
	@Column(name = "age")
	int age;
	
	@Column(name = "gender")
	String gender;
	
	@Column(name = "dob", nullable = true)
	String dob;

	@Column(name = "contact")
	long contact;
	
	@Column(name = "alt_contact", nullable = true)
	Long altContact;
	
	@Column(name = "email", nullable = true)
	String email;
	
	@Column(name = "password")
	String password;
	
	@Column(name = "status")
	int status;

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public long getContact() {
		return contact;
	}

	public void setContact(long contact) {
		this.contact = contact;
	}

	public Long getAltContact() {
		return altContact;
	}

	public void setAltContact(Long altContact) {
		this.altContact = altContact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static void setManagerDetails(ManagerDetails managerDetails) {
		ManagerDetails.managerDetails = managerDetails;
	}



}
public class ManagerDetailsService implements IManagerDetailsService {

	@Autowired
	ManagerDao managerDao;
	@Transactional
	@Override
	public ManagerDetails saveManagerDetails(ManagerDetails managerDetails) {
	return managerDao.saveManagerDetails(managerDetails);	

	}
	@Transactional
	@Override
	public String managerLogin(LoginDetails loginDetails) {
		
		return managerDao.managerLogin(loginDetails);
	}
	@Override
	public ManagerDetails getManagerDetails(int i) {
		// TODO Auto-generated method stub
		return managerDao.getManagerDetails(i);
	}


}
public class PilotController {

	@Resource(name = "pilotService")
	IPilotDao pilotService;

	@RequestMapping(value = "/viewAllPilots", method = RequestMethod.GET) // displaying the pilot details with edit
																			// button
	public ModelAndView pilotlist() {
		ModelAndView modelAndView = new ModelAndView("viewPilot");
		List<PilotDetails> pd = pilotService.pilotreqList();
		modelAndView.addObject("pilotList", pd);

		return modelAndView;
	}

	@RequestMapping(value = "/editpilotdet", method = RequestMethod.POST) // action for the edit button
	public ModelAndView editpilotdet(int pilotid) {
		ModelAndView modelAndView = new ModelAndView("editPilot");
		PilotDetails pd = pilotService.editPilotDetails(pilotid);
		modelAndView.addObject("pilot", pd);

		return modelAndView;
	}

	@RequestMapping(value = "/saveEditpilotdet", method = RequestMethod.POST) // updating the pilot details in the db
	public String saveeditpilotdet(PilotDetails pd) {

		pilotService.saveEditPilotDetails(pd);

		return "redirect:/app/viewAllPilots";
	}

	@RequestMapping(value = "/Addpilot", method = RequestMethod.GET)
	String addPilot() {
		return "addPilot";
	}

	@RequestMapping(value = "/addPilotDetails", method = RequestMethod.POST)
	public String addPilot(PilotDetails pilotDetails) {
		pilotService.addPilotDetails(pilotDetails);
		return "redirect:/app/viewAllPilots";
	}

	@RequestMapping(value = "/removepilot", method = RequestMethod.POST)
	public String removePilot(int pilotId) {
		pilotService.removePilotDetails(pilotId);
		return "redirect:/app/viewAllPilots";
	}

}
public class PilotDao implements IPilotDao {

	public PilotDao() {
		// TODO Auto-generated constructor stub
	}

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public int addPilotDetails(PilotDetails pilotDetails) {
		// TODO Auto-generated method stub
		entityManager.persist(pilotDetails);
		return 1;
	}

	@Override
	public int removePilotDetails(int pid) {
		PilotDetails details = entityManager.find(PilotDetails.class, pid);
		entityManager.remove(details);
		return 1;
	}

	@Override
	public List<PilotDetails> pilotreqList() { // Listing all pilot details
		// TODO Auto-generated method stub
		List<PilotDetails> li = null;
		String hql = "Select l from PilotDetails l ";
		TypedQuery<PilotDetails> query = entityManager.createQuery(hql, PilotDetails.class);

		li = query.getResultList();

		return li;
	}

	@Override
	public PilotDetails editPilotDetails(int id) { // Listing pilot details to edit
		// TODO Auto-generated method stub

		PilotDetails details = entityManager.find(PilotDetails.class, id);

		return details;
	}


	@Override
	public void saveEditPilotDetails(PilotDetails pd) { // saving and updating the details into DB
		// TODO Auto-generated method stub

		entityManager.merge(pd);
	}

	@Override
	public PilotDetails getPilotDetails(int pid) {
		// TODO Auto-generated method stub
		return entityManager.find(PilotDetails.class, pid);
	}

}
public class PilotDetails {

	public PilotDetails() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="pilot_id")
	private int pilotId;
	
	@Column(name="license_number")
	private String licNo;
	
	@Column(name="address1")
	private String address1;
	
	@Column(name="address2")
	private String address2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="zipcode")
	private String zipcode;
	
	@Column(name="ssn")
	private int ssn;

	@Override
	public String toString() {
		return "PilotDetails [pilotId=" + pilotId + ", licNo=" + licNo + ", address1=" + address1 + ", address2="
				+ address2 + ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + ", ssn=" + ssn + "]";
	}

	public int getPilotId() {
		return pilotId;
	}

	public void setPilotId(int pilotId) {
		this.pilotId = pilotId;
	}

	public String getLicNo() {
		return licNo;
	}

	public void setLicNo(String licNo) {
		this.licNo = licNo;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public int getSsn() {
		return ssn;
	}

	public void setSsn(int ssn) {
		this.ssn = ssn;
	}
}
public class PilotPlaneDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "S_no")
	private int sNo;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pilots_pilot_id")
	private PilotDetails pilotDetails;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "planes_plane_id")
	private PlaneDetails planeDetails;

	public int getsNo() {
		return sNo;
	}

	public void setsNo(int sNo) {
		this.sNo = sNo;
	}

	public PilotDetails getPilotDetails() {
		return pilotDetails;
	}

	public void setPilotDetails(PilotDetails pilotDetails) {
		this.pilotDetails = pilotDetails;
	}

	public PlaneDetails getPlaneDetails() {
		return planeDetails;
	}

	public void setPlaneDetails(PlaneDetails planeDetails) {
		this.planeDetails = planeDetails;
	}

	@Override
	public String toString() {
		return "PilotPlaneDetails [sNo=" + sNo + ", pilotDetails=" + pilotDetails + ", planeDetails=" + planeDetails
				+ "]";
	}

}
public class PilotService implements IPilotDao {
   
	
	@Autowired
	PilotDao dao;	
	
	public PilotService() {
		// TODO Auto-generated constructor stub
	}
	@Transactional
	@Override
	public int addPilotDetails(PilotDetails pilotDetails) {
		// TODO Auto-generated method stub
		return dao.addPilotDetails(pilotDetails);
	}
	
	@Transactional
	@Override
	public int removePilotDetails(int pid) {
		// TODO Auto-generated method stub
		return dao.removePilotDetails(pid);
	}

	@Override
	public List<PilotDetails> pilotreqList() {
		// TODO Auto-generated method stub
		return dao.pilotreqList();
	}
	@Transactional
	@Override
	public PilotDetails editPilotDetails(int id) {
		// TODO Auto-generated method stub
		return dao.editPilotDetails(id);
	}

	@Transactional
	@Override
	public void saveEditPilotDetails(PilotDetails pd) {
		// TODO Auto-generated method stub
		dao.saveEditPilotDetails(pd);
	}


	@Override
	public PilotDetails getPilotDetails(int pid) {
		// TODO Auto-generated method stub
		return dao.getPilotDetails(pid);
	}

}
public class PlaneController {

	@Resource(name = "AddPlaneService")
	IAddPlane addPlaneDaoservice;

	@RequestMapping(value = "/addPlane", method = RequestMethod.GET)
	String addproductPage() {
		return "addPlane"; // JSP name of add plane page
	}

	@RequestMapping(value = "/AddPlaneController", method = RequestMethod.POST)
	public String addPlane(PlaneDetails planeDetails) {
		addPlaneDaoservice.addPlane(planeDetails);
		return "redirect:/app/viewAllPlanes";
	}

	@RequestMapping(value = "/viewAllPlanes", method = RequestMethod.GET)
	public ModelAndView viewPlanes() {
		ModelAndView modelAndView = new ModelAndView("viewPlane");
		List<PlaneDetails> li = addPlaneDaoservice.viewAllPlanes();
		modelAndView.addObject("planeList", li);
		return modelAndView;
	}

	@RequestMapping(value = "/ViewPlaneDetail", method = RequestMethod.GET)
	public ModelAndView planeDetail(Integer pid) {
		ModelAndView modelAndView = new ModelAndView("editPlane");
		modelAndView.addObject("planeDetails", addPlaneDaoservice.viewDetails(pid));
		return modelAndView;
	}

	@RequestMapping(value = "/updatePlane", method = RequestMethod.POST)
	public String updatePlane(PlaneDetails pd) {
		addPlaneDaoservice.updatePlane(pd);
		return "redirect:/app/viewAllPlanes";
	}

}
public class PlaneDetails {
	@Id
	@Column(name="plane_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int planeId;
	@Column(name="owner_id")
	private int ownerId;
	@Column(name="owner_first_name")
	private String ownerFirstName;
	@Column(name="owner_last_name")
	private String ownerLastName;
	@Column(name="owner_contact")
	private int ownerContact;
	@Column(name="owner_email")
	private String ownerEmail;
	@Column(name="plane_type")
	private String planeType;
	@Column(name="plane_capacity")
	private int planeCapacity;
	
	public int getPlaneId() {
		return planeId;
	}
	public void setPlaneId(int planeId) {
		this.planeId = planeId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerFirstName() {
		return ownerFirstName;
	}
	public void setOwnerFirstName(String ownerFirstName) {
		this.ownerFirstName = ownerFirstName;
	}
	public String getOwnerLastName() {
		return ownerLastName;
	}
	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}
	public int getOwnerContact() {
		return ownerContact;
	}
	public void setOwnerContact(int ownerContact) {
		this.ownerContact = ownerContact;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	public String getPlaneType() {
		return planeType;
	}
	public void setPlaneType(String planeType) {
		this.planeType = planeType;
	}
	public int getPlaneCapacity() {
		return planeCapacity;
	}
	public void setPlaneCapacity(int planeCapacity) {
		this.planeCapacity = planeCapacity;
	}
	
	@Override
	public String toString() {
		return "PlaneDetails [planeId=" + planeId + ", ownerId=" + ownerId + ", ownerFirstName=" + ownerFirstName
				+ ", ownerLastName=" + ownerLastName + ", ownerContact=" + ownerContact + ", ownerEmail=" + ownerEmail
				+ ", planeType=" + planeType + ", planeCapacity=" + planeCapacity + "]";
	}
	
	
	
}
public class PlaneHangarStatus {
	
	@Id
	@Column(name="S_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sNo;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="planes_plane_id")
	private PlaneDetails planeDetails;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="hangar_hangar_id")
	private HangarDetails hangarDetails;

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public PlaneDetails getPlaneDetails() {
		return planeDetails;
	}

	public void setPlaneDetails(PlaneDetails planeDetails) {
		this.planeDetails = planeDetails;
	}

	public HangarDetails getHangarDetails() {
		return hangarDetails;
	}

	public void setHangarDetails(HangarDetails hangarDetails) {
		this.hangarDetails = hangarDetails;
	}

	public PlaneHangarStatus(Integer sNo, PlaneDetails planeDetails, HangarDetails hangarDetails) {
		super();
		this.sNo = sNo;
		this.planeDetails = planeDetails;
		this.hangarDetails = hangarDetails;
	}

	public PlaneHangarStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PlaneHangarStatus [sNo=" + sNo + ", planeDetails=" + planeDetails + ", hangarDetails=" + hangarDetails
				+ "]";
	}

}
public class UnassignPlane {

	@Resource(name="unassignPlaneService")
	IUnassignPlane unassignPlane;
	
	@Resource(name="assignHangarPlaneService")
	IAssignHangarPlane assignHangarPlane;
	
	@RequestMapping(value="/checkhangarstatus",method=RequestMethod.GET)
	public ModelAndView checkHangar(String hangarId) {
		ModelAndView modelAndview=new ModelAndView("HangarStatus");
		List<HangarStatus> li=unassignPlane.getAssignedHangar();
		System.out.println(li.size());
		modelAndview.addObject("assignedHangarList",li);
		modelAndview.addObject("unassignedHangarList",assignHangarPlane.getUnassignedHangar());
		return modelAndview;
	}
	
	@RequestMapping(value="/unassignHangar",method=RequestMethod.POST)
	public String unassignHangar(String hangarId) {
		unassignPlane.unassignHangar(hangarId);
		return "redirect:/app/checkhangarstatus";
	}
}
public class UnassignPlanesDao implements IUnassignPlane {

	@PersistenceContext
	public EntityManager entityManager;
	
	@Override
	public List<HangarStatus> getAssignedHangar() {
		System.out.println("\n\n\n\n\n\n\n\\n\n\n\n\\n\n\nin method");
		TypedQuery<HangarStatus> query=entityManager.createQuery("Select h from HangarStatus h where h.status='N'",HangarStatus.class);
		return query.getResultList();
	}

	@Override
	public void unassignHangar(String hangarId) {
		System.out.println("\n\n\\n\n\n\n\n\n"+hangarId);
		TypedQuery<HangarStatus> query=entityManager.createQuery("Select h from HangarStatus h where h.hangarDetails.hangarId="+hangarId,HangarStatus.class);
		HangarStatus hangarStatus=query.getSingleResult();
		hangarStatus.setAvailableFD(null);
		hangarStatus.setAvailableTD(null);
		hangarStatus.setStatus("A");
		hangarStatus.setOccupancyFD(null);
		hangarStatus.setOccupancyTD(null);
		TypedQuery<PlaneHangarStatus> q=entityManager.createQuery("Select ph from PlaneHangarStatus ph where ph.hangarDetails.hangarId="+hangarId,PlaneHangarStatus.class);
		PlaneHangarStatus planeHangarStatus=q.getSingleResult();
		entityManager.remove(planeHangarStatus);
	}

}
public class UnassignPlaneService implements IUnassignPlane{

	@Autowired
	IUnassignPlane unassignPlane;
	@Override
	public List<HangarStatus> getAssignedHangar() {
		// TODO Auto-generated method stub
		return unassignPlane.getAssignedHangar();
	}
	@Transactional
	@Override
	public void unassignHangar(String hangarId) {
	unassignPlane.unassignHangar(hangarId);
		
	}

}
