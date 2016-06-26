# example Makefile from 
# http://myweb.stedwards.src/src/edu/laurab/help/javamakefile.html
#JFLAGS = -g
JFLAGS = 
JC = javac
CDIR = build/classes
SDIR = ./src
CP = .:build/classes:lib/jspamga.jar:lib/tablelayout.jar
RM = /bin/rm -rf
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) -sourcepath $(SDIR) -classpath $(CP) -d $(CDIR) $*.java

CLASSES = \
	./src/edu/gmu/cds/opt/GARun.java \
	./src/edu/gmu/cds/opt/OptEntity.java \
	./src/edu/gmu/cds/opt/OptSorter.java \
	./src/edu/gmu/cds/opt/FitnessEvaluator.java \
	./src/edu/gmu/cds/opt/GA.java \
	./src/edu/gmu/cds/opt/SimpleEvaluator.java \
	./src/edu/gmu/cds/xml/XmlReader.java \
	./src/edu/gmu/cds/xml/XmlWriter.java \
	./src/edu/gmu/cds/xml/XmlTag.java \
	./src/edu/gmu/cds/util/MouseUtil.java \
	./src/edu/gmu/cds/util/ApplicationProperties.java \
	./src/edu/gmu/cds/util/URLUtil.java \
	./src/edu/gmu/cds/util/FilterUtil.java \
	./src/edu/gmu/cds/util/MathUtil.java \
	./src/edu/gmu/cds/util/ZipUtil.java \
	./src/edu/gmu/cds/util/Base64Util.java \
	./src/edu/gmu/cds/util/PropertyUtil.java \
	./src/edu/gmu/cds/util/CryptUtil.java \
	./src/edu/gmu/cds/util/PerformanceTest.java \
	./src/edu/gmu/cds/util/FileUtil.java \
	./src/edu/gmu/cds/util/SimRunUtil.java \
	./src/edu/gmu/cds/util/ResourceManager.java \
	./src/edu/gmu/cds/util/MonitorUtil.java \
	./src/edu/gmu/cds/util/BaseProperties.java \
	./src/edu/gmu/cds/util/OrbitUtil.java \
	./src/edu/gmu/cds/util/RotationUtil.java \
	./src/edu/gmu/cds/util/StyleUtil.java \
	./src/edu/gmu/cds/img/ImageCoordinateModel.java \
	./src/edu/gmu/cds/img/ZernikeSorter.java \
	./src/edu/gmu/cds/img/ZernikeMoment.java \
	./src/edu/gmu/cds/img/ContourPoint.java \
	./src/edu/gmu/cds/img/ImageDownloader.java \
	./src/edu/gmu/cds/img/HuAnalysis.java \
	./src/edu/gmu/cds/img/ImageData.java \
	./src/edu/gmu/cds/img/PixelGrouper.java \
	./src/edu/gmu/cds/img/ImageListener.java \
	./src/edu/gmu/cds/img/HuMoment.java \
	./src/edu/gmu/cds/img/ContourFinder.java \
	./src/edu/gmu/cds/img/ImageProcessor.java \
	./src/edu/gmu/cds/sim/opt/StateInfoCombiner.java \
	./src/edu/gmu/cds/sim/opt/SimOptEntity.java \
	./src/edu/gmu/cds/sim/opt/SimEvaluator.java \
	./src/edu/gmu/cds/sim/SimImageGenerator.java \
	./src/edu/gmu/cds/sim/StateInfoSorter.java \
	./src/edu/gmu/cds/sim/SimUtil.java \
	./src/edu/gmu/cds/sim/DiskInfo.java \
	./src/edu/gmu/cds/sim/SimSummary.java \
	./src/edu/gmu/cds/sim/StateGenerator.java \
	./src/edu/gmu/cds/sim/StateInfo.java \
	./src/edu/gmu/cds/sim/TargetData.java \
	./src/edu/gmu/cds/sim/StateInfoFile.java \
	./src/edu/gmu/cds/sim/Constants.java \
	./src/edu/gmu/cds/sim/MergerWars.java \
	./src/edu/gmu/cds/stats/Histogram.java \
	./src/edu/gmu/cds/stats/HistogramPanel.java \
	./src/edu/gmu/cds/ui/StateInfoTableModel.java \
	./src/edu/gmu/cds/ui/RotationController.java \
	./src/edu/gmu/cds/ui/ImagePanel.java \
	./src/edu/gmu/cds/ui/DiskInfoImagePanel.java \
	./src/edu/gmu/cds/ui/SquareKeeper.java \
	./src/edu/gmu/cds/ui/QuaternionRotator.java \
	./src/edu/gmu/cds/ui/search/StateClickListener.java \
	./src/edu/gmu/cds/ui/search/SimPanel.java \
	./src/edu/gmu/cds/ui/search/StatsPanel.java \
	./src/edu/gmu/cds/ui/search/EnhancePanel.java \
	./src/edu/gmu/cds/ui/search/SimRunner.java \
	./src/edu/gmu/cds/ui/search/EvaluatePanel.java \
	./src/edu/gmu/cds/ui/search/ReviewPanel.java \
	./src/edu/gmu/cds/ui/search/SimPrefPanel.java \
	./src/edu/gmu/cds/ui/search/SimulationHandler.java \
	./src/edu/gmu/cds/ui/search/MainPanel.java \
	./src/edu/gmu/cds/ui/search/StateInfoImageListPanel.java \
	./src/edu/gmu/cds/ui/PublicPainter.java \
	./src/edu/gmu/cds/ui/ObjectLocationQueryPanel.java \
	./src/edu/gmu/cds/ui/target/DiskInfoPanel.java \
	./src/edu/gmu/cds/ui/target/ParameterRangePanel.java \
	./src/edu/gmu/cds/ui/target/DiskSetupPanel.java \
	./src/edu/gmu/cds/ui/target/ThumbnailEditor.java \
	./src/edu/gmu/cds/ui/target/DiskOrientationPanel.java \
	./src/edu/gmu/cds/ui/target/TargetPanel.java \
	./src/edu/gmu/cds/ui/target/DiskSelectionPanel.java \
	./src/edu/gmu/cds/ui/target/MainPanel.java \
	./src/edu/gmu/cds/ui/TableLayoutPanel.java \
	./src/edu/gmu/cds/ui/ScatterPanel.java \
	./src/edu/gmu/cds/ui/contour/ZernikeContourPanel.java \
	./src/edu/gmu/cds/ui/contour/MainContourPanel.java \
	./src/edu/gmu/cds/ui/helper/ObjectInfoHelper.java \
	./src/edu/gmu/cds/ui/helper/UIHelper.java \
	./src/edu/gmu/cds/ui/helper/MaskHelper.java \
	./src/edu/gmu/cds/ui/helper/TabHelper.java \
	./src/edu/gmu/cds/ui/helper/ClipboardHelper.java \
	./src/edu/gmu/cds/ui/helper/PositionInfoHelper.java \
	./src/edu/gmu/cds/ui/slider/Slider2D.java \
	./src/edu/gmu/cds/ui/slider/Slider2DListener.java \
	./src/edu/gmu/cds/ui/slider/CrossHairPanel.java \
	./src/edu/gmu/cds/ui/slider/DualSliderPanel.java \
	./src/edu/gmu/cds/ui/StarPlot.java \
	./src/edu/gmu/cds/ui/CursorInfoPanel.java \
	./src/edu/gmu/cds/sdss/Mass2Light.java \
	./src/edu/gmu/cds/sdss/SDSSCasQuery.java \
	./src/edu/gmu/cds/sdss/SDSSObject.java \
	./src/edu/gmu/cds/obj/ObjectQuery.java \
	./src/edu/gmu/cds/obj/ObjectInfoListener.java \
	./src/edu/gmu/cds/obj/ObjectInfo.java \
	./src/edu/gmu/cds/obj/RADec.java

default: all

all: classes jar

initdirs:
	mkdir -p build
	mkdir -p build/classes
	mkdir -p build/jar

classes: initdirs $(CLASSES:.java=.class)

clean:
	$(RM) build

jar:
	jar cvf build/jar/MergerEx.jar -C $(CDIR) edu 
