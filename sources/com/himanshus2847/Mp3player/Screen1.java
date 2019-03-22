package com.himanshus2847.Mp3player;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AppInventorCompatActivity;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Camera;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EmotionRecognizer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Image;
import com.google.appinventor.components.runtime.ImagePicker;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.Player;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.RetValManager;
import com.google.appinventor.components.runtime.util.RuntimeErrorAlert;
import com.google.youngandroid.runtime;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.Format;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.functions.IsEqual;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.reflect.SlotGet;
import gnu.kawa.reflect.SlotSet;
import gnu.lists.Consumer;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.Procedure;
import gnu.mapping.PropertySet;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import kawa.lang.Promise;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.strings;
import kawa.standard.Scheme;
import kawa.standard.require;

/* compiled from: Screen1.yail */
public class Screen1 extends Form implements Runnable {
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("Screen1").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("getMessage").readResolve());
    static final IntNum Lit10;
    static final FString Lit100 = new FString("com.google.appinventor.components.runtime.EmotionRecognizer");
    static final SimpleSymbol Lit101 = ((SimpleSymbol) new SimpleSymbol("$mostLikelyEmotion").readResolve());
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol("$mostLikelyEmotionScore").readResolve());
    static final PairWithPosition Lit103 = PairWithPosition.make(Lit132, PairWithPosition.make(Lit132, LList.Empty, "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 696597), "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 696592);
    static final SimpleSymbol Lit104 = ((SimpleSymbol) new SimpleSymbol("Player2").readResolve());
    static final SimpleSymbol Lit105 = ((SimpleSymbol) new SimpleSymbol("Start").readResolve());
    static final PairWithPosition Lit106 = PairWithPosition.make(Lit132, PairWithPosition.make(Lit132, LList.Empty, "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 696785), "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 696780);
    static final PairWithPosition Lit107 = PairWithPosition.make(Lit132, PairWithPosition.make(Lit132, LList.Empty, "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 696973), "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 696968);
    static final SimpleSymbol Lit108 = ((SimpleSymbol) new SimpleSymbol("Player1").readResolve());
    static final PairWithPosition Lit109 = PairWithPosition.make(Lit132, PairWithPosition.make(Lit132, LList.Empty, "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 697159), "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 697154);
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("number").readResolve());
    static final SimpleSymbol Lit110 = ((SimpleSymbol) new SimpleSymbol("Player3").readResolve());
    static final SimpleSymbol Lit111 = ((SimpleSymbol) new SimpleSymbol("Emotion_Recognizer1$GotResponse").readResolve());
    static final SimpleSymbol Lit112 = ((SimpleSymbol) new SimpleSymbol("GotResponse").readResolve());
    static final FString Lit113 = new FString("com.google.appinventor.components.runtime.Player");
    static final SimpleSymbol Lit114 = ((SimpleSymbol) new SimpleSymbol("Source").readResolve());
    static final FString Lit115 = new FString("com.google.appinventor.components.runtime.Player");
    static final FString Lit116 = new FString("com.google.appinventor.components.runtime.Player");
    static final FString Lit117 = new FString("com.google.appinventor.components.runtime.Player");
    static final FString Lit118 = new FString("com.google.appinventor.components.runtime.Player");
    static final FString Lit119 = new FString("com.google.appinventor.components.runtime.Player");
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("ShowStatusBar").readResolve());
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("android-log-form").readResolve());
    static final SimpleSymbol Lit121 = ((SimpleSymbol) new SimpleSymbol("add-to-form-environment").readResolve());
    static final SimpleSymbol Lit122 = ((SimpleSymbol) new SimpleSymbol("lookup-in-form-environment").readResolve());
    static final SimpleSymbol Lit123 = ((SimpleSymbol) new SimpleSymbol("is-bound-in-form-environment").readResolve());
    static final SimpleSymbol Lit124 = ((SimpleSymbol) new SimpleSymbol("add-to-global-var-environment").readResolve());
    static final SimpleSymbol Lit125 = ((SimpleSymbol) new SimpleSymbol("add-to-events").readResolve());
    static final SimpleSymbol Lit126 = ((SimpleSymbol) new SimpleSymbol("add-to-components").readResolve());
    static final SimpleSymbol Lit127 = ((SimpleSymbol) new SimpleSymbol("add-to-global-vars").readResolve());
    static final SimpleSymbol Lit128 = ((SimpleSymbol) new SimpleSymbol("add-to-form-do-after-creation").readResolve());
    static final SimpleSymbol Lit129 = ((SimpleSymbol) new SimpleSymbol("send-error").readResolve());
    static final SimpleSymbol Lit13 = ((SimpleSymbol) new SimpleSymbol("Title").readResolve());
    static final SimpleSymbol Lit130 = ((SimpleSymbol) new SimpleSymbol("dispatchEvent").readResolve());
    static final SimpleSymbol Lit131 = ((SimpleSymbol) new SimpleSymbol("lookup-handler").readResolve());
    static final SimpleSymbol Lit132 = ((SimpleSymbol) new SimpleSymbol("any").readResolve());
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("TitleBarColor").readResolve());
    static final IntNum Lit15;
    static final FString Lit16 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("Horizontal_Arrangement10").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("Height").readResolve());
    static final IntNum Lit19 = IntNum.make(-1005);
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("*the-null-value*").readResolve());
    static final FString Lit20 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit21 = new FString("com.google.appinventor.components.runtime.Image");
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("Image2").readResolve());
    static final IntNum Lit23 = IntNum.make(-1010);
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("Picture").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("Width").readResolve());
    static final IntNum Lit26 = IntNum.make(-1080);
    static final FString Lit27 = new FString("com.google.appinventor.components.runtime.Image");
    static final FString Lit28 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("Horizontal_Arrangement7").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("ActionBar").readResolve());
    static final IntNum Lit30 = IntNum.make(-1002);
    static final FString Lit31 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit32 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit33 = ((SimpleSymbol) new SimpleSymbol("Button1").readResolve());
    static final SimpleSymbol Lit34 = ((SimpleSymbol) new SimpleSymbol("FontBold").readResolve());
    static final SimpleSymbol Lit35 = ((SimpleSymbol) new SimpleSymbol("FontSize").readResolve());
    static final IntNum Lit36 = IntNum.make(20);
    static final IntNum Lit37 = IntNum.make(-1008);
    static final SimpleSymbol Lit38 = ((SimpleSymbol) new SimpleSymbol("Shape").readResolve());
    static final IntNum Lit39 = IntNum.make(1);
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN).readResolve());
    static final SimpleSymbol Lit40 = ((SimpleSymbol) new SimpleSymbol("Text").readResolve());
    static final IntNum Lit41 = IntNum.make(-1080);
    static final FString Lit42 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit43 = ((SimpleSymbol) new SimpleSymbol("Camera1").readResolve());
    static final SimpleSymbol Lit44 = ((SimpleSymbol) new SimpleSymbol("TakePicture").readResolve());
    static final SimpleSymbol Lit45 = ((SimpleSymbol) new SimpleSymbol("Button1$Click").readResolve());
    static final SimpleSymbol Lit46 = ((SimpleSymbol) new SimpleSymbol("Click").readResolve());
    static final FString Lit47 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit48 = ((SimpleSymbol) new SimpleSymbol("Horizontal_Arrangement5").readResolve());
    static final IntNum Lit49 = IntNum.make(-1002);
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("AppName").readResolve());
    static final FString Lit50 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit51 = new FString("com.google.appinventor.components.runtime.ImagePicker");
    static final SimpleSymbol Lit52 = ((SimpleSymbol) new SimpleSymbol("Image_Picker1").readResolve());
    static final IntNum Lit53 = IntNum.make(-1008);
    static final IntNum Lit54 = IntNum.make(-1080);
    static final FString Lit55 = new FString("com.google.appinventor.components.runtime.ImagePicker");
    static final SimpleSymbol Lit56 = ((SimpleSymbol) new SimpleSymbol("Emotion_Recognizer1").readResolve());
    static final SimpleSymbol Lit57 = ((SimpleSymbol) new SimpleSymbol("postImage").readResolve());
    static final SimpleSymbol Lit58 = ((SimpleSymbol) new SimpleSymbol("Selection").readResolve());
    static final PairWithPosition Lit59 = PairWithPosition.make(Lit6, LList.Empty, "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 364667);
    static final SimpleSymbol Lit6;
    static final SimpleSymbol Lit60 = ((SimpleSymbol) new SimpleSymbol("Image1").readResolve());
    static final SimpleSymbol Lit61 = ((SimpleSymbol) new SimpleSymbol("Image_Picker1$AfterPicking").readResolve());
    static final SimpleSymbol Lit62 = ((SimpleSymbol) new SimpleSymbol("AfterPicking").readResolve());
    static final FString Lit63 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("Horizontal_Arrangement3").readResolve());
    static final IntNum Lit65 = IntNum.make(-1005);
    static final FString Lit66 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit67 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("Horizontal_Arrangement4").readResolve());
    static final IntNum Lit69 = IntNum.make(-1030);
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("BackgroundImage").readResolve());
    static final IntNum Lit70 = IntNum.make(-1080);
    static final FString Lit71 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit72 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit73 = ((SimpleSymbol) new SimpleSymbol("Horizontal_Arrangement1").readResolve());
    static final SimpleSymbol Lit74 = ((SimpleSymbol) new SimpleSymbol("AlignVertical").readResolve());
    static final IntNum Lit75 = IntNum.make(2);
    static final IntNum Lit76 = IntNum.make(-1030);
    static final IntNum Lit77 = IntNum.make(-1080);
    static final FString Lit78 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit79 = new FString("com.google.appinventor.components.runtime.Image");
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("Icon").readResolve());
    static final IntNum Lit80 = IntNum.make(-1030);
    static final IntNum Lit81 = IntNum.make(-1070);
    static final FString Lit82 = new FString("com.google.appinventor.components.runtime.Image");
    static final FString Lit83 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit84 = ((SimpleSymbol) new SimpleSymbol("Horizontal_Arrangement6").readResolve());
    static final IntNum Lit85 = IntNum.make(-1005);
    static final FString Lit86 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit87 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit88 = ((SimpleSymbol) new SimpleSymbol("Label1").readResolve());
    static final FString Lit89 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("PrimaryColorDark").readResolve());
    static final FString Lit90 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol("Label2").readResolve());
    static final FString Lit92 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit93 = new FString("com.google.appinventor.components.runtime.Camera");
    static final FString Lit94 = new FString("com.google.appinventor.components.runtime.Camera");
    static final SimpleSymbol Lit95 = ((SimpleSymbol) new SimpleSymbol("$image").readResolve());
    static final PairWithPosition Lit96;
    static final SimpleSymbol Lit97 = ((SimpleSymbol) new SimpleSymbol("Camera1$AfterPicture").readResolve());
    static final SimpleSymbol Lit98 = ((SimpleSymbol) new SimpleSymbol("AfterPicture").readResolve());
    static final FString Lit99 = new FString("com.google.appinventor.components.runtime.EmotionRecognizer");
    public static Screen1 Screen1;
    static final ModuleMethod lambda$Fn1 = null;
    static final ModuleMethod lambda$Fn10 = null;
    static final ModuleMethod lambda$Fn11 = null;
    static final ModuleMethod lambda$Fn12 = null;
    static final ModuleMethod lambda$Fn13 = null;
    static final ModuleMethod lambda$Fn14 = null;
    static final ModuleMethod lambda$Fn15 = null;
    static final ModuleMethod lambda$Fn16 = null;
    static final ModuleMethod lambda$Fn17 = null;
    static final ModuleMethod lambda$Fn18 = null;
    static final ModuleMethod lambda$Fn19 = null;
    static final ModuleMethod lambda$Fn2 = null;
    static final ModuleMethod lambda$Fn20 = null;
    static final ModuleMethod lambda$Fn21 = null;
    static final ModuleMethod lambda$Fn22 = null;
    static final ModuleMethod lambda$Fn23 = null;
    static final ModuleMethod lambda$Fn24 = null;
    static final ModuleMethod lambda$Fn25 = null;
    static final ModuleMethod lambda$Fn26 = null;
    static final ModuleMethod lambda$Fn27 = null;
    static final ModuleMethod lambda$Fn28 = null;
    static final ModuleMethod lambda$Fn29 = null;
    static final ModuleMethod lambda$Fn3 = null;
    static final ModuleMethod lambda$Fn30 = null;
    static final ModuleMethod lambda$Fn31 = null;
    static final ModuleMethod lambda$Fn32 = null;
    static final ModuleMethod lambda$Fn33 = null;
    static final ModuleMethod lambda$Fn34 = null;
    static final ModuleMethod lambda$Fn4 = null;
    static final ModuleMethod lambda$Fn5 = null;
    static final ModuleMethod lambda$Fn6 = null;
    static final ModuleMethod lambda$Fn7 = null;
    static final ModuleMethod lambda$Fn8 = null;
    static final ModuleMethod lambda$Fn9 = null;
    public Boolean $Stdebug$Mnform$St;
    public final ModuleMethod $define;
    public Button Button1;
    public final ModuleMethod Button1$Click;
    public Camera Camera1;
    public final ModuleMethod Camera1$AfterPicture;
    public EmotionRecognizer Emotion_Recognizer1;
    public final ModuleMethod Emotion_Recognizer1$GotResponse;
    public HorizontalArrangement Horizontal_Arrangement1;
    public HorizontalArrangement Horizontal_Arrangement10;
    public HorizontalArrangement Horizontal_Arrangement3;
    public HorizontalArrangement Horizontal_Arrangement4;
    public HorizontalArrangement Horizontal_Arrangement5;
    public HorizontalArrangement Horizontal_Arrangement6;
    public HorizontalArrangement Horizontal_Arrangement7;
    public Image Image1;
    public Image Image2;
    public ImagePicker Image_Picker1;
    public final ModuleMethod Image_Picker1$AfterPicking;
    public Label Label1;
    public Label Label2;
    public Player Player1;
    public Player Player2;
    public Player Player3;
    public final ModuleMethod add$Mnto$Mncomponents;
    public final ModuleMethod add$Mnto$Mnevents;
    public final ModuleMethod add$Mnto$Mnform$Mndo$Mnafter$Mncreation;
    public final ModuleMethod add$Mnto$Mnform$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvar$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvars;
    public final ModuleMethod android$Mnlog$Mnform;
    public LList components$Mnto$Mncreate;
    public final ModuleMethod dispatchEvent;
    public LList events$Mnto$Mnregister;
    public LList form$Mndo$Mnafter$Mncreation;
    public Environment form$Mnenvironment;
    public Symbol form$Mnname$Mnsymbol;
    public Environment global$Mnvar$Mnenvironment;
    public LList global$Mnvars$Mnto$Mncreate;
    public final ModuleMethod is$Mnbound$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod lookup$Mnhandler;
    public final ModuleMethod lookup$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod onCreate;
    public final ModuleMethod process$Mnexception;
    public final ModuleMethod send$Mnerror;

    /* compiled from: Screen1.yail */
    public class frame extends ModuleBody {
        Screen1 $main = this;

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 1:
                    if (!(obj instanceof Screen1)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 2:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 4:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 6:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 11:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 12:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 13:
                    if (!(obj instanceof Screen1)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 47:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 3:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 4:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 7:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 8:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 10:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 15:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }

        public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 9:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.value3 = obj3;
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                case 14:
                    if (!(obj instanceof Screen1)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    if (!(obj2 instanceof Component)) {
                        return -786430;
                    }
                    callContext.value2 = obj2;
                    if (!(obj3 instanceof String)) {
                        return -786429;
                    }
                    callContext.value3 = obj3;
                    if (!(obj4 instanceof String)) {
                        return -786428;
                    }
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                case 48:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.value3 = obj3;
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                default:
                    return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
            }
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 1:
                    try {
                        this.$main.onCreate((Bundle) obj);
                        return Values.empty;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "onCreate", 1, obj);
                    }
                case 2:
                    this.$main.androidLogForm(obj);
                    return Values.empty;
                case 4:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "lookup-in-form-environment", 1, obj);
                    }
                case 6:
                    try {
                        return this.$main.isBoundInFormEnvironment((Symbol) obj) ? Boolean.TRUE : Boolean.FALSE;
                    } catch (ClassCastException e22) {
                        throw new WrongType(e22, "is-bound-in-form-environment", 1, obj);
                    }
                case 11:
                    this.$main.addToFormDoAfterCreation(obj);
                    return Values.empty;
                case 12:
                    this.$main.sendError(obj);
                    return Values.empty;
                case 13:
                    this.$main.processException(obj);
                    return Values.empty;
                case 47:
                    return this.$main.Camera1$AfterPicture(obj);
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
            switch (moduleMethod.selector) {
                case 9:
                    this.$main.addToComponents(obj, obj2, obj3, obj4);
                    return Values.empty;
                case 14:
                    try {
                        try {
                            try {
                                try {
                                    return this.$main.dispatchEvent((Component) obj, (String) obj2, (String) obj3, (Object[]) obj4) ? Boolean.TRUE : Boolean.FALSE;
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "dispatchEvent", 4, obj4);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "dispatchEvent", 3, obj3);
                            }
                        } catch (ClassCastException e22) {
                            throw new WrongType(e22, "dispatchEvent", 2, obj2);
                        }
                    } catch (ClassCastException e222) {
                        throw new WrongType(e222, "dispatchEvent", 1, obj);
                    }
                case 48:
                    return this.$main.Emotion_Recognizer1$GotResponse(obj, obj2, obj3, obj4);
                default:
                    return super.apply4(moduleMethod, obj, obj2, obj3, obj4);
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 3:
                    try {
                        this.$main.addToFormEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "add-to-form-environment", 1, obj);
                    }
                case 4:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj, obj2);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "lookup-in-form-environment", 1, obj);
                    }
                case 7:
                    try {
                        this.$main.addToGlobalVarEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e22) {
                        throw new WrongType(e22, "add-to-global-var-environment", 1, obj);
                    }
                case 8:
                    this.$main.addToEvents(obj, obj2);
                    return Values.empty;
                case 10:
                    this.$main.addToGlobalVars(obj, obj2);
                    return Values.empty;
                case 15:
                    return this.$main.lookupHandler(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 16:
                    return Screen1.lambda2();
                case 17:
                    this.$main.$define();
                    return Values.empty;
                case 18:
                    return Screen1.lambda3();
                case 19:
                    return Screen1.lambda4();
                case 20:
                    return Screen1.lambda5();
                case 21:
                    return Screen1.lambda6();
                case 22:
                    return Screen1.lambda7();
                case 23:
                    return Screen1.lambda8();
                case 24:
                    return Screen1.lambda9();
                case 25:
                    return Screen1.lambda10();
                case 26:
                    return Screen1.lambda11();
                case 27:
                    return this.$main.Button1$Click();
                case 28:
                    return Screen1.lambda12();
                case 29:
                    return Screen1.lambda13();
                case 30:
                    return Screen1.lambda14();
                case 31:
                    return Screen1.lambda15();
                case 32:
                    return this.$main.Image_Picker1$AfterPicking();
                case 33:
                    return Screen1.lambda16();
                case 34:
                    return Screen1.lambda17();
                case 35:
                    return Screen1.lambda18();
                case 36:
                    return Screen1.lambda19();
                case 37:
                    return Screen1.lambda20();
                case 38:
                    return Screen1.lambda21();
                case 39:
                    return Screen1.lambda22();
                case 40:
                    return Screen1.lambda23();
                case 41:
                    return Screen1.lambda24();
                case 42:
                    return Screen1.lambda25();
                case 43:
                    return Screen1.lambda26();
                case 44:
                    return Screen1.lambda27();
                case 45:
                    return Screen1.lambda28();
                case 46:
                    return Screen1.lambda29();
                case 49:
                    return Screen1.lambda30();
                case 50:
                    return Screen1.lambda31();
                case 51:
                    return Screen1.lambda32();
                case 52:
                    return Screen1.lambda33();
                case 53:
                    return Screen1.lambda34();
                case 54:
                    return Screen1.lambda35();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 16:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 17:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 18:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 19:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 20:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 21:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 22:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 23:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 24:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 25:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 26:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 27:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 28:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 29:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 30:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 31:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 32:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 33:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 34:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 35:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 36:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 37:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 38:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 39:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 40:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 41:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 42:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 43:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 44:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 45:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 46:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 49:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 50:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 51:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 52:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 53:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 54:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }
    }

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_TEXT).readResolve();
        Lit6 = simpleSymbol;
        Lit96 = PairWithPosition.make(simpleSymbol, LList.Empty, "/tmp/1553079593526_0.5611361102323578-0/youngandroidproject/../src/com/himanshus2847/Mp3player/Screen1.yail", 667753);
        int[] iArr = new int[2];
        iArr[0] = Component.COLOR_BLUE;
        Lit15 = IntNum.make(iArr);
        iArr = new int[2];
        iArr[0] = -16777216;
        Lit10 = IntNum.make(iArr);
    }

    public Screen1() {
        ModuleInfo.register(this);
        ModuleBody frame = new frame();
        this.onCreate = new ModuleMethod(frame, 1, "onCreate", FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.android$Mnlog$Mnform = new ModuleMethod(frame, 2, Lit120, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.add$Mnto$Mnform$Mnenvironment = new ModuleMethod(frame, 3, Lit121, 8194);
        this.lookup$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 4, Lit122, 8193);
        this.is$Mnbound$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame, 6, Lit123, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.add$Mnto$Mnglobal$Mnvar$Mnenvironment = new ModuleMethod(frame, 7, Lit124, 8194);
        this.add$Mnto$Mnevents = new ModuleMethod(frame, 8, Lit125, 8194);
        this.add$Mnto$Mncomponents = new ModuleMethod(frame, 9, Lit126, 16388);
        this.add$Mnto$Mnglobal$Mnvars = new ModuleMethod(frame, 10, Lit127, 8194);
        this.add$Mnto$Mnform$Mndo$Mnafter$Mncreation = new ModuleMethod(frame, 11, Lit128, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.send$Mnerror = new ModuleMethod(frame, 12, Lit129, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.process$Mnexception = new ModuleMethod(frame, 13, "process-exception", FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.dispatchEvent = new ModuleMethod(frame, 14, Lit130, 16388);
        this.lookup$Mnhandler = new ModuleMethod(frame, 15, Lit131, 8194);
        PropertySet moduleMethod = new ModuleMethod(frame, 16, null, 0);
        moduleMethod.setProperty("source-location", "/tmp/runtime6278664814273186053.scm:581");
        lambda$Fn1 = moduleMethod;
        this.$define = new ModuleMethod(frame, 17, "$define", 0);
        lambda$Fn2 = new ModuleMethod(frame, 18, null, 0);
        lambda$Fn3 = new ModuleMethod(frame, 19, null, 0);
        lambda$Fn4 = new ModuleMethod(frame, 20, null, 0);
        lambda$Fn5 = new ModuleMethod(frame, 21, null, 0);
        lambda$Fn6 = new ModuleMethod(frame, 22, null, 0);
        lambda$Fn7 = new ModuleMethod(frame, 23, null, 0);
        lambda$Fn8 = new ModuleMethod(frame, 24, null, 0);
        lambda$Fn9 = new ModuleMethod(frame, 25, null, 0);
        lambda$Fn10 = new ModuleMethod(frame, 26, null, 0);
        this.Button1$Click = new ModuleMethod(frame, 27, Lit45, 0);
        lambda$Fn11 = new ModuleMethod(frame, 28, null, 0);
        lambda$Fn12 = new ModuleMethod(frame, 29, null, 0);
        lambda$Fn13 = new ModuleMethod(frame, 30, null, 0);
        lambda$Fn14 = new ModuleMethod(frame, 31, null, 0);
        this.Image_Picker1$AfterPicking = new ModuleMethod(frame, 32, Lit61, 0);
        lambda$Fn15 = new ModuleMethod(frame, 33, null, 0);
        lambda$Fn16 = new ModuleMethod(frame, 34, null, 0);
        lambda$Fn17 = new ModuleMethod(frame, 35, null, 0);
        lambda$Fn18 = new ModuleMethod(frame, 36, null, 0);
        lambda$Fn19 = new ModuleMethod(frame, 37, null, 0);
        lambda$Fn20 = new ModuleMethod(frame, 38, null, 0);
        lambda$Fn21 = new ModuleMethod(frame, 39, null, 0);
        lambda$Fn22 = new ModuleMethod(frame, 40, null, 0);
        lambda$Fn23 = new ModuleMethod(frame, 41, null, 0);
        lambda$Fn24 = new ModuleMethod(frame, 42, null, 0);
        lambda$Fn25 = new ModuleMethod(frame, 43, null, 0);
        lambda$Fn26 = new ModuleMethod(frame, 44, null, 0);
        lambda$Fn27 = new ModuleMethod(frame, 45, null, 0);
        lambda$Fn28 = new ModuleMethod(frame, 46, null, 0);
        this.Camera1$AfterPicture = new ModuleMethod(frame, 47, Lit97, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.Emotion_Recognizer1$GotResponse = new ModuleMethod(frame, 48, Lit111, 16388);
        lambda$Fn29 = new ModuleMethod(frame, 49, null, 0);
        lambda$Fn30 = new ModuleMethod(frame, 50, null, 0);
        lambda$Fn31 = new ModuleMethod(frame, 51, null, 0);
        lambda$Fn32 = new ModuleMethod(frame, 52, null, 0);
        lambda$Fn33 = new ModuleMethod(frame, 53, null, 0);
        lambda$Fn34 = new ModuleMethod(frame, 54, null, 0);
    }

    public Object lookupInFormEnvironment(Symbol symbol) {
        return lookupInFormEnvironment(symbol, Boolean.FALSE);
    }

    public void run() {
        Throwable th;
        CallContext instance = CallContext.getInstance();
        Consumer consumer = instance.consumer;
        instance.consumer = VoidConsumer.instance;
        try {
            run(instance);
            th = null;
        } catch (Throwable th2) {
            th = th2;
        }
        ModuleBody.runCleanup(instance, th, consumer);
    }

    public final void run(CallContext $ctx) {
        Consumer $result = $ctx.consumer;
        Object find = require.find("com.google.youngandroid.runtime");
        try {
            String str;
            ((Runnable) find).run();
            this.$Stdebug$Mnform$St = Boolean.FALSE;
            this.form$Mnenvironment = Environment.make(misc.symbol$To$String(Lit0));
            FString stringAppend = strings.stringAppend(misc.symbol$To$String(Lit0), "-global-vars");
            if (stringAppend == null) {
                str = null;
            } else {
                str = stringAppend.toString();
            }
            this.global$Mnvar$Mnenvironment = Environment.make(str);
            Screen1 = null;
            this.form$Mnname$Mnsymbol = Lit0;
            this.events$Mnto$Mnregister = LList.Empty;
            this.components$Mnto$Mncreate = LList.Empty;
            this.global$Mnvars$Mnto$Mncreate = LList.Empty;
            this.form$Mndo$Mnafter$Mncreation = LList.Empty;
            find = require.find("com.google.youngandroid.runtime");
            try {
                ((Runnable) find).run();
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit3, Boolean.TRUE, Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit5, "Mp3player", Lit6);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit7, "676b5298f7bbcc6f16251837f76eab5b.jpg", Lit6);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit8, "kisspng-smiley-emoticon-clip-art-faces-5abdeb889fe966.741165541522396040655.jpg", Lit6);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit9, Lit10, Lit11);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit12, Boolean.FALSE, Lit4);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit13, "Emo Player", Lit6);
                    Values.writeValues(runtime.setAndCoerceProperty$Ex(Lit0, Lit14, Lit15, Lit11), $result);
                } else {
                    addToFormDoAfterCreation(new Promise(lambda$Fn2));
                }
                this.Horizontal_Arrangement10 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit16, Lit17, lambda$Fn3), $result);
                } else {
                    addToComponents(Lit0, Lit20, Lit17, lambda$Fn4);
                }
                this.Image2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit21, Lit22, lambda$Fn5), $result);
                } else {
                    addToComponents(Lit0, Lit27, Lit22, lambda$Fn6);
                }
                this.Horizontal_Arrangement7 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit28, Lit29, lambda$Fn7), $result);
                } else {
                    addToComponents(Lit0, Lit31, Lit29, lambda$Fn8);
                }
                this.Button1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit32, Lit33, lambda$Fn9), $result);
                } else {
                    addToComponents(Lit0, Lit42, Lit33, lambda$Fn10);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit45, this.Button1$Click);
                } else {
                    addToFormEnvironment(Lit45, this.Button1$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Button1", "Click");
                } else {
                    addToEvents(Lit33, Lit46);
                }
                this.Horizontal_Arrangement5 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit47, Lit48, lambda$Fn11), $result);
                } else {
                    addToComponents(Lit0, Lit50, Lit48, lambda$Fn12);
                }
                this.Image_Picker1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit51, Lit52, lambda$Fn13), $result);
                } else {
                    addToComponents(Lit0, Lit55, Lit52, lambda$Fn14);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit61, this.Image_Picker1$AfterPicking);
                } else {
                    addToFormEnvironment(Lit61, this.Image_Picker1$AfterPicking);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Image_Picker1", "AfterPicking");
                } else {
                    addToEvents(Lit52, Lit62);
                }
                this.Horizontal_Arrangement3 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit63, Lit64, lambda$Fn15), $result);
                } else {
                    addToComponents(Lit0, Lit66, Lit64, lambda$Fn16);
                }
                this.Horizontal_Arrangement4 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit67, Lit68, lambda$Fn17), $result);
                } else {
                    addToComponents(Lit0, Lit71, Lit68, lambda$Fn18);
                }
                this.Horizontal_Arrangement1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit72, Lit73, lambda$Fn19), $result);
                } else {
                    addToComponents(Lit68, Lit78, Lit73, lambda$Fn20);
                }
                this.Image1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit73, Lit79, Lit60, lambda$Fn21), $result);
                } else {
                    addToComponents(Lit73, Lit82, Lit60, lambda$Fn22);
                }
                this.Horizontal_Arrangement6 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit83, Lit84, lambda$Fn23), $result);
                } else {
                    addToComponents(Lit0, Lit86, Lit84, lambda$Fn24);
                }
                this.Label1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit87, Lit88, lambda$Fn25), $result);
                } else {
                    addToComponents(Lit0, Lit89, Lit88, lambda$Fn26);
                }
                this.Label2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit90, Lit91, lambda$Fn27), $result);
                } else {
                    addToComponents(Lit0, Lit92, Lit91, lambda$Fn28);
                }
                this.Camera1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit93, Lit43, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit94, Lit43, Boolean.FALSE);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit97, this.Camera1$AfterPicture);
                } else {
                    addToFormEnvironment(Lit97, this.Camera1$AfterPicture);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Camera1", "AfterPicture");
                } else {
                    addToEvents(Lit43, Lit98);
                }
                this.Emotion_Recognizer1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit99, Lit56, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit100, Lit56, Boolean.FALSE);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit111, this.Emotion_Recognizer1$GotResponse);
                } else {
                    addToFormEnvironment(Lit111, this.Emotion_Recognizer1$GotResponse);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Emotion_Recognizer1", "GotResponse");
                } else {
                    addToEvents(Lit56, Lit112);
                }
                this.Player1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit113, Lit108, lambda$Fn29), $result);
                } else {
                    addToComponents(Lit0, Lit115, Lit108, lambda$Fn30);
                }
                this.Player2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit116, Lit104, lambda$Fn31), $result);
                } else {
                    addToComponents(Lit0, Lit117, Lit104, lambda$Fn32);
                }
                this.Player3 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit118, Lit110, lambda$Fn33), $result);
                } else {
                    addToComponents(Lit0, Lit119, Lit110, lambda$Fn34);
                }
                runtime.initRuntime();
            } catch (ClassCastException e) {
                throw new WrongType(e, "java.lang.Runnable.run()", 1, find);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "java.lang.Runnable.run()", 1, find);
        }
    }

    static Object lambda3() {
        runtime.setAndCoerceProperty$Ex(Lit0, Lit3, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit5, "Mp3player", Lit6);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit7, "676b5298f7bbcc6f16251837f76eab5b.jpg", Lit6);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit8, "kisspng-smiley-emoticon-clip-art-faces-5abdeb889fe966.741165541522396040655.jpg", Lit6);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit9, Lit10, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit12, Boolean.FALSE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit13, "Emo Player", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit0, Lit14, Lit15, Lit11);
    }

    static Object lambda4() {
        return runtime.setAndCoerceProperty$Ex(Lit17, Lit18, Lit19, Lit11);
    }

    static Object lambda5() {
        return runtime.setAndCoerceProperty$Ex(Lit17, Lit18, Lit19, Lit11);
    }

    static Object lambda6() {
        runtime.setAndCoerceProperty$Ex(Lit22, Lit18, Lit23, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit22, Lit24, "reactions_12_2017.png", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit22, Lit25, Lit26, Lit11);
    }

    static Object lambda7() {
        runtime.setAndCoerceProperty$Ex(Lit22, Lit18, Lit23, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit22, Lit24, "reactions_12_2017.png", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit22, Lit25, Lit26, Lit11);
    }

    static Object lambda8() {
        return runtime.setAndCoerceProperty$Ex(Lit29, Lit18, Lit30, Lit11);
    }

    static Object lambda9() {
        return runtime.setAndCoerceProperty$Ex(Lit29, Lit18, Lit30, Lit11);
    }

    static Object lambda10() {
        runtime.setAndCoerceProperty$Ex(Lit33, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit35, Lit36, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit18, Lit37, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit38, Lit39, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit40, "Take image from camera", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit33, Lit25, Lit41, Lit11);
    }

    static Object lambda11() {
        runtime.setAndCoerceProperty$Ex(Lit33, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit35, Lit36, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit18, Lit37, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit38, Lit39, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit33, Lit40, "Take image from camera", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit33, Lit25, Lit41, Lit11);
    }

    public Object Button1$Click() {
        runtime.setThisForm();
        return runtime.callComponentMethod(Lit43, Lit44, LList.Empty, LList.Empty);
    }

    static Object lambda12() {
        return runtime.setAndCoerceProperty$Ex(Lit48, Lit18, Lit49, Lit11);
    }

    static Object lambda13() {
        return runtime.setAndCoerceProperty$Ex(Lit48, Lit18, Lit49, Lit11);
    }

    static Object lambda14() {
        runtime.setAndCoerceProperty$Ex(Lit52, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit35, Lit36, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit18, Lit53, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit38, Lit39, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit40, "Take image from gallery", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit52, Lit25, Lit54, Lit11);
    }

    static Object lambda15() {
        runtime.setAndCoerceProperty$Ex(Lit52, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit35, Lit36, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit18, Lit53, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit38, Lit39, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit52, Lit40, "Take image from gallery", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit52, Lit25, Lit54, Lit11);
    }

    public Object Image_Picker1$AfterPicking() {
        runtime.setThisForm();
        runtime.callComponentMethod(Lit56, Lit57, LList.list1(runtime.getProperty$1(Lit52, Lit58)), Lit59);
        return runtime.setAndCoerceProperty$Ex(Lit60, Lit24, runtime.getProperty$1(Lit52, Lit58), Lit6);
    }

    static Object lambda16() {
        return runtime.setAndCoerceProperty$Ex(Lit64, Lit18, Lit65, Lit11);
    }

    static Object lambda17() {
        return runtime.setAndCoerceProperty$Ex(Lit64, Lit18, Lit65, Lit11);
    }

    static Object lambda18() {
        runtime.setAndCoerceProperty$Ex(Lit68, Lit18, Lit69, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit68, Lit25, Lit70, Lit11);
    }

    static Object lambda19() {
        runtime.setAndCoerceProperty$Ex(Lit68, Lit18, Lit69, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit68, Lit25, Lit70, Lit11);
    }

    static Object lambda20() {
        runtime.setAndCoerceProperty$Ex(Lit73, Lit74, Lit75, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit73, Lit18, Lit76, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit73, Lit25, Lit77, Lit11);
    }

    static Object lambda21() {
        runtime.setAndCoerceProperty$Ex(Lit73, Lit74, Lit75, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit73, Lit18, Lit76, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit73, Lit25, Lit77, Lit11);
    }

    static Object lambda22() {
        runtime.setAndCoerceProperty$Ex(Lit60, Lit18, Lit80, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit60, Lit24, "Images-icon.png", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit60, Lit25, Lit81, Lit11);
    }

    static Object lambda23() {
        runtime.setAndCoerceProperty$Ex(Lit60, Lit18, Lit80, Lit11);
        runtime.setAndCoerceProperty$Ex(Lit60, Lit24, "Images-icon.png", Lit6);
        return runtime.setAndCoerceProperty$Ex(Lit60, Lit25, Lit81, Lit11);
    }

    static Object lambda24() {
        return runtime.setAndCoerceProperty$Ex(Lit84, Lit18, Lit85, Lit11);
    }

    static Object lambda25() {
        return runtime.setAndCoerceProperty$Ex(Lit84, Lit18, Lit85, Lit11);
    }

    static Object lambda26() {
        runtime.setAndCoerceProperty$Ex(Lit88, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit35, Lit36, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit88, Lit40, "Emotion", Lit6);
    }

    static Object lambda27() {
        runtime.setAndCoerceProperty$Ex(Lit88, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit35, Lit36, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit88, Lit40, "Emotion", Lit6);
    }

    static Object lambda28() {
        runtime.setAndCoerceProperty$Ex(Lit91, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit35, Lit36, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit91, Lit40, "Score", Lit6);
    }

    static Object lambda29() {
        runtime.setAndCoerceProperty$Ex(Lit91, Lit34, Boolean.TRUE, Lit4);
        runtime.setAndCoerceProperty$Ex(Lit91, Lit35, Lit36, Lit11);
        return runtime.setAndCoerceProperty$Ex(Lit91, Lit40, "Score", Lit6);
    }

    public Object Camera1$AfterPicture(Object $image) {
        Object signalRuntimeError;
        $image = runtime.sanitizeComponentData($image);
        runtime.setThisForm();
        SimpleSymbol simpleSymbol = Lit56;
        SimpleSymbol simpleSymbol2 = Lit57;
        if ($image instanceof Package) {
            signalRuntimeError = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit95), " is not bound in the current context"), "Unbound Variable");
        } else {
            signalRuntimeError = $image;
        }
        runtime.callComponentMethod(simpleSymbol, simpleSymbol2, LList.list1(signalRuntimeError), Lit96);
        SimpleSymbol simpleSymbol3 = Lit60;
        simpleSymbol = Lit24;
        if ($image instanceof Package) {
            $image = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit95), " is not bound in the current context"), "Unbound Variable");
        }
        return runtime.setAndCoerceProperty$Ex(simpleSymbol3, simpleSymbol, $image, Lit6);
    }

    public Object Emotion_Recognizer1$GotResponse(Object $responseCode, Object $responseContent, Object $mostLikelyEmotion, Object $mostLikelyEmotionScore) {
        runtime.sanitizeComponentData($responseCode);
        runtime.sanitizeComponentData($responseContent);
        $mostLikelyEmotion = runtime.sanitizeComponentData($mostLikelyEmotion);
        $mostLikelyEmotionScore = runtime.sanitizeComponentData($mostLikelyEmotionScore);
        runtime.setThisForm();
        SimpleSymbol simpleSymbol = Lit88;
        SimpleSymbol simpleSymbol2 = Lit40;
        if ($mostLikelyEmotion instanceof Package) {
            $mostLikelyEmotion = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit101), " is not bound in the current context"), "Unbound Variable");
        }
        runtime.setAndCoerceProperty$Ex(simpleSymbol, simpleSymbol2, $mostLikelyEmotion, Lit6);
        simpleSymbol = Lit91;
        simpleSymbol2 = Lit40;
        if ($mostLikelyEmotionScore instanceof Package) {
            $mostLikelyEmotionScore = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit102), " is not bound in the current context"), "Unbound Variable");
        }
        runtime.setAndCoerceProperty$Ex(simpleSymbol, simpleSymbol2, $mostLikelyEmotionScore, Lit6);
        if (runtime.callYailPrimitive(runtime.yail$Mnequal$Qu, LList.list2(runtime.getProperty$1(Lit88, Lit40), "happiness"), Lit103, "=") != Boolean.FALSE) {
            runtime.callComponentMethod(Lit104, Lit105, LList.Empty, LList.Empty);
        }
        if (runtime.callYailPrimitive(runtime.yail$Mnequal$Qu, LList.list2(runtime.getProperty$1(Lit88, Lit40), "neutral"), Lit106, "=") != Boolean.FALSE) {
            runtime.callComponentMethod(Lit104, Lit105, LList.Empty, LList.Empty);
        }
        if (runtime.callYailPrimitive(runtime.yail$Mnequal$Qu, LList.list2(runtime.getProperty$1(Lit88, Lit40), "sadness"), Lit107, "=") != Boolean.FALSE) {
            runtime.callComponentMethod(Lit108, Lit105, LList.Empty, LList.Empty);
        }
        return runtime.callYailPrimitive(runtime.yail$Mnequal$Qu, LList.list2(runtime.getProperty$1(Lit88, Lit40), "anger"), Lit109, "=") != Boolean.FALSE ? runtime.callComponentMethod(Lit110, Lit105, LList.Empty, LList.Empty) : Values.empty;
    }

    static Object lambda30() {
        return runtime.setAndCoerceProperty$Ex(Lit108, Lit114, "It_Happens.mp3", Lit6);
    }

    static Object lambda31() {
        return runtime.setAndCoerceProperty$Ex(Lit108, Lit114, "It_Happens.mp3", Lit6);
    }

    static Object lambda32() {
        return runtime.setAndCoerceProperty$Ex(Lit104, Lit114, "Urban_Lullaby.mp3", Lit6);
    }

    static Object lambda33() {
        return runtime.setAndCoerceProperty$Ex(Lit104, Lit114, "Urban_Lullaby.mp3", Lit6);
    }

    static Object lambda34() {
        return runtime.setAndCoerceProperty$Ex(Lit110, Lit114, "Barge_Sting.mp3", Lit6);
    }

    static Object lambda35() {
        return runtime.setAndCoerceProperty$Ex(Lit110, Lit114, "Barge_Sting.mp3", Lit6);
    }

    public void onCreate(Bundle icicle) {
        AppInventorCompatActivity.setClassicModeFromYail(true);
        super.onCreate(icicle);
    }

    public void androidLogForm(Object message) {
    }

    public void addToFormEnvironment(Symbol name, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name, this.form$Mnenvironment, object));
        this.form$Mnenvironment.put(name, object);
    }

    public Object lookupInFormEnvironment(Symbol name, Object default$Mnvalue) {
        boolean x = ((this.form$Mnenvironment == null ? 1 : 0) + 1) & 1;
        if (x) {
            if (!this.form$Mnenvironment.isBound(name)) {
                return default$Mnvalue;
            }
        } else if (!x) {
            return default$Mnvalue;
        }
        return this.form$Mnenvironment.get(name);
    }

    public boolean isBoundInFormEnvironment(Symbol name) {
        return this.form$Mnenvironment.isBound(name);
    }

    public void addToGlobalVarEnvironment(Symbol name, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name, this.global$Mnvar$Mnenvironment, object));
        this.global$Mnvar$Mnenvironment.put(name, object);
    }

    public void addToEvents(Object component$Mnname, Object event$Mnname) {
        this.events$Mnto$Mnregister = lists.cons(lists.cons(component$Mnname, event$Mnname), this.events$Mnto$Mnregister);
    }

    public void addToComponents(Object container$Mnname, Object component$Mntype, Object component$Mnname, Object init$Mnthunk) {
        this.components$Mnto$Mncreate = lists.cons(LList.list4(container$Mnname, component$Mntype, component$Mnname, init$Mnthunk), this.components$Mnto$Mncreate);
    }

    public void addToGlobalVars(Object var, Object val$Mnthunk) {
        this.global$Mnvars$Mnto$Mncreate = lists.cons(LList.list2(var, val$Mnthunk), this.global$Mnvars$Mnto$Mncreate);
    }

    public void addToFormDoAfterCreation(Object thunk) {
        this.form$Mndo$Mnafter$Mncreation = lists.cons(thunk, this.form$Mndo$Mnafter$Mncreation);
    }

    public void sendError(Object error) {
        RetValManager.sendError(error == null ? null : error.toString());
    }

    public void processException(Object ex) {
        Object apply1 = Scheme.applyToArgs.apply1(GetNamedPart.getNamedPart.apply2(ex, Lit1));
        RuntimeErrorAlert.alert(this, apply1 == null ? null : apply1.toString(), ex instanceof YailRuntimeError ? ((YailRuntimeError) ex).getErrorType() : "Runtime Error", "End Application");
    }

    public boolean dispatchEvent(Component componentObject, String registeredComponentName, String eventName, Object[] args) {
        boolean x;
        SimpleSymbol registeredObject = misc.string$To$Symbol(registeredComponentName);
        if (!isBoundInFormEnvironment(registeredObject)) {
            EventDispatcher.unregisterEventForDelegation(this, registeredComponentName, eventName);
            return false;
        } else if (lookupInFormEnvironment(registeredObject) != componentObject) {
            return false;
        } else {
            try {
                Scheme.apply.apply2(lookupHandler(registeredComponentName, eventName), LList.makeList(args, 0));
                return true;
            } catch (PermissionException exception) {
                exception.printStackTrace();
                if (this == componentObject) {
                    x = true;
                } else {
                    x = false;
                }
                if (x ? IsEqual.apply(eventName, "PermissionNeeded") : x) {
                    processException(exception);
                } else {
                    PermissionDenied(componentObject, eventName, exception.getPermissionNeeded());
                }
                return false;
            } catch (Throwable exception2) {
                androidLogForm(exception2.getMessage());
                exception2.printStackTrace();
                processException(exception2);
                return false;
            }
        }
    }

    public Object lookupHandler(Object componentName, Object eventName) {
        String str = null;
        String obj = componentName == null ? null : componentName.toString();
        if (eventName != null) {
            str = eventName.toString();
        }
        return lookupInFormEnvironment(misc.string$To$Symbol(EventDispatcher.makeFullEventName(obj, str)));
    }

    public void $define() {
        Language.setDefaults(Scheme.getInstance());
        try {
            run();
        } catch (Exception exception) {
            androidLogForm(exception.getMessage());
            processException(exception);
        }
        Screen1 = this;
        addToFormEnvironment(Lit0, this);
        Object obj = this.events$Mnto$Mnregister;
        while (obj != LList.Empty) {
            try {
                Pair arg0 = (Pair) obj;
                Object event$Mninfo = arg0.getCar();
                Object apply1 = lists.car.apply1(event$Mninfo);
                String obj2 = apply1 == null ? null : apply1.toString();
                Object apply12 = lists.cdr.apply1(event$Mninfo);
                EventDispatcher.registerEventForDelegation(this, obj2, apply12 == null ? null : apply12.toString());
                obj = arg0.getCdr();
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, obj);
            }
        }
        addToGlobalVars(Lit2, lambda$Fn1);
        Screen1 closureEnv = this;
        obj = lists.reverse(this.global$Mnvars$Mnto$Mncreate);
        while (obj != LList.Empty) {
            try {
                arg0 = (Pair) obj;
                Object var$Mnval = arg0.getCar();
                Object var = lists.car.apply1(var$Mnval);
                addToGlobalVarEnvironment((Symbol) var, Scheme.applyToArgs.apply1(lists.cadr.apply1(var$Mnval)));
                obj = arg0.getCdr();
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "arg0", -2, obj);
            } catch (ClassCastException e22) {
                throw new WrongType(e22, "arg0", -2, obj);
            } catch (ClassCastException e222) {
                throw new WrongType(e222, "add-to-form-environment", 0, component$Mnname);
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "lookup-in-form-environment", 0, apply1);
            } catch (ClassCastException e2222) {
                throw new WrongType(e2222, "arg0", -2, obj);
            } catch (ClassCastException e22222) {
                throw new WrongType(e22222, "arg0", -2, obj);
            } catch (ClassCastException e222222) {
                throw new WrongType(e222222, "add-to-global-var-environment", 0, var);
            } catch (ClassCastException e2222222) {
                throw new WrongType(e2222222, "arg0", -2, obj);
            } catch (YailRuntimeError exception2) {
                processException(exception2);
                return;
            }
        }
        obj = lists.reverse(this.form$Mndo$Mnafter$Mncreation);
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            misc.force(arg0.getCar());
            obj = arg0.getCdr();
        }
        LList component$Mndescriptors = lists.reverse(this.components$Mnto$Mncreate);
        closureEnv = this;
        obj = component$Mndescriptors;
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            Object component$Mninfo = arg0.getCar();
            Object component$Mnname = lists.caddr.apply1(component$Mninfo);
            lists.cadddr.apply1(component$Mninfo);
            Object component$Mnobject = Invoke.make.apply2(lists.cadr.apply1(component$Mninfo), lookupInFormEnvironment((Symbol) lists.car.apply1(component$Mninfo)));
            SlotSet.set$Mnfield$Ex.apply3(this, component$Mnname, component$Mnobject);
            addToFormEnvironment((Symbol) component$Mnname, component$Mnobject);
            obj = arg0.getCdr();
        }
        obj = component$Mndescriptors;
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            component$Mninfo = arg0.getCar();
            lists.caddr.apply1(component$Mninfo);
            Boolean init$Mnthunk = lists.cadddr.apply1(component$Mninfo);
            if (init$Mnthunk != Boolean.FALSE) {
                Scheme.applyToArgs.apply1(init$Mnthunk);
            }
            obj = arg0.getCdr();
        }
        obj = component$Mndescriptors;
        while (obj != LList.Empty) {
            arg0 = (Pair) obj;
            component$Mninfo = arg0.getCar();
            component$Mnname = lists.caddr.apply1(component$Mninfo);
            lists.cadddr.apply1(component$Mninfo);
            callInitialize(SlotGet.field.apply2(this, component$Mnname));
            obj = arg0.getCdr();
        }
    }

    public static SimpleSymbol lambda1symbolAppend$V(Object[] argsArray) {
        Object car;
        LList symbols = LList.makeList(argsArray, 0);
        Procedure procedure = Scheme.apply;
        ModuleMethod moduleMethod = strings.string$Mnappend;
        Pair result = LList.Empty;
        Object arg0 = symbols;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                car = arg02.getCar();
                try {
                    result = Pair.make(misc.symbol$To$String((Symbol) car), result);
                    arg0 = arg03;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "symbol->string", 1, car);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "arg0", -2, arg0);
            }
        }
        car = procedure.apply2(moduleMethod, LList.reverseInPlace(result));
        try {
            return misc.string$To$Symbol((CharSequence) car);
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "string->symbol", 1, car);
        }
    }

    static Object lambda2() {
        return null;
    }
}
