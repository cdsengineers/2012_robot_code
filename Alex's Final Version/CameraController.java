
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class CameraController
{

    private AxisCamera camera;          
    private CriteriaCollection cc;
    
    public CameraController()
    {
        camera = AxisCamera.getInstance("192.168.0.90");
        camera.writeMaxFPS(10);
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        
        cc = new CriteriaCollection();
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
    }
    
    public void refreshTargets()
    {
        try
        {
            ColorImage image = camera.getImage();     // comment if using stored images
            BinaryImage thresholdImage = image.thresholdRGB(25, 255, 0, 45, 0, 47);   // keep only red objects
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles

            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results

            System.out.println("\n\nFo und "+filteredImage.getNumberParticles() + " boxes");
            for (int i = 0; i < reports.length; i++) {                                // print results
                ParticleAnalysisReport r = reports[i];
                System.out.println("Square: ");
                System.out.println("Center x: " + r.center_mass_x);
                System.out.println("Center Y: "+r.center_mass_y);
                System.out.println("Box Area: "+r.particleArea+" px^2");
            }

            System.out.println("Match Size: "+filteredImage.getOrderedParticleAnalysisReports(1)[0].particleToImagePercent);

            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();
        } catch (Exception ex) {
            
        }
    }
}