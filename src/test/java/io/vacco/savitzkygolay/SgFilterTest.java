package io.vacco.savitzkygolay;

import com.google.gson.GsonBuilder;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.util.stream.*;

import static io.vacco.sabnock.SkJson.*;
import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class SgFilterTest {
  static {
    it("Computes approximate values on a sine signal", () -> {
      int testSize = 64;

      float[]
        ys = new float[testSize],
        xs = new float[testSize],
        out = new float[testSize];

      for (int i = 0; i < testSize; i++) {
        ys[i] = (float) (20 * Math.sin(i/Math.PI/6));
        xs[i] = i;
      }

      var noisy = new double[] {
        1.7436529959960687,1.7860754612031657,3.3091264200695463,2.24578807081874,
        1.9973600416625716,7.5793943303154485,6.829006779193456,4.832989374267338,
        6.403820383557631,9.249739993574218,7.822351583094823,10.291711819741924,
        13.358966165358261,13.146406989357702,13.539871649446166,13.244507861948417,

        14.897178558432614,15.322187602862833,15.04746304310164,18.956388717174132,
        18.94415528030369,15.97910824216843,17.204265330782402,19.075501332384782,
        20.088932926252784,20.226557977759274,19.09880113161894,18.52591538347622,
        19.728312486702034,20.72383130848788,19.61449140549318,19.251890687262552,

        20.044430741579603,21.611423826707405,20.522726803308665,20.679943885331227,
        16.863878388601375,17.556842606742023,18.539130418108577,18.00848359779573,
        16.18141774933029,16.206483084406955,16.320177959630758,14.680729210041337,
        15.053301332997545,11.887452145152578,11.525080248963718,11.625717895522637,

        9.531535152103112,10.108011312962638,10.953608223142194,9.19756906073447,
        8.635097779068158,7.248246253488856,5.88356785879013,3.5979388806949952,
        2.495415115664354,3.859921123822152,0.4091871255440711,2.054964426911227,
        -0.6771754386123763,0.2441129210505646,-4.974976132176126,-2.474193416090937
      };

      var noisyF = new float[noisy.length];
      for (int i = 0; i < noisyF.length; i++) {
        noisyF[i] = (float) noisy[i];
      }

      var sgf = new SgFilter(7);
      sgf.process(noisyF, xs, out);

      var g = new GsonBuilder().setPrettyPrinting().create();
      var chart = obj(
        kv("legend", obj(
          kv("data", new String[] { "measurement", "KfEstimate" })
        )),
        kv("tooltip", obj(kv("trigger", "axis"))),
        kv("grid", obj(kv("containLabel", "true"))),
        kv("xAxis", obj(
          kv("type", "category"),
          kv("data", IntStream.range(0, xs.length).boxed().collect(Collectors.toList()))
        )),
        kv("yAxis", obj(kv("type", "value"))),
        kv("dataZoom", new Object[] { obj(kv("type", "inside")), obj() }),
        kv("series", new Object[] {
          obj(
            kv("name", "ground-truth"),
            kv("type", "line"),
            kv("data", ys)
          ),
          obj(
            kv("name", "noise-signal"),
            kv("type", "line"),
            kv("data", noisy),
            kv("lineStyle", obj(
              kv("color", "#5470C6"),
              kv("width", 2),
              kv("type", "dashed")
            ))
          ),
          obj(
            kv("name", "sg-estimate"),
            kv("type", "line"),
            kv("data", out)
          )
        })
      );

      System.out.println(g.toJson(chart));
    });
  }
}
